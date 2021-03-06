package net.minecraftforge.mercurius;

import com.google.common.collect.Maps;
import com.google.gson.Gson;

import net.minecraftforge.mercurius.dataModels.StatsPingModel;
import net.minecraftforge.mercurius.dataModels.StatsStartModel;
import net.minecraftforge.mercurius.helpers.DataHelper;
import net.minecraftforge.mercurius.helpers.StatsConstants;
import net.minecraftforge.mercurius.utils.Commands;
import net.minecraftforge.mercurius.utils.GameEnvironment;
import net.minecraftforge.mercurius.utils.LogHelper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.Map.Entry;

// Sender class that does the logic to send data to Mercurius servers. Ugly and needs refactoring most likely.
public class Sender
{
    public StatsPingModel data;
    String sessionIDApp = DataHelper.CreateID();

    public boolean isSnooperDisabled()
    {
        return Mercurius.getBinding().isSnooperDisabled();
    }

    public String toJSON(StatsPingModel model) {
        Gson json = new Gson();

        return json.toJson(model);
    }

    public void collectData(Commands cmd, boolean upload) throws Exception
    {
        collectData(cmd, upload, Mercurius.getBinding().getGameEnvironment());
    }

    public void collectData(Commands cmd, boolean upload, GameEnvironment environment) throws Exception
    {
        // Is snooper disabled?
        if (this.isSnooperDisabled())
        {
            // ABORT Mission!!!
            LogHelper.info("Snooper is disabled... aborting collection.");
            return;
        }

        LogHelper.info("Starting collecting data for event "+cmd.toString());

        // Build the model and populate with the data received from the mod.
        StatsPingModel model = cmd.newInstance();
        model.InstallID           = GlobalConfig.installID; // Grab the install ID.
        model.SessionID           = Mercurius.getBinding().getSessionID(); // Grab the session ID from the Mod.
        model.ClientSessionID     = sessionIDApp; // Grab the client session ID from ourselves since we control that.
        model.Mods                = Mercurius.getBinding().gatherModData(cmd); // Grab all the mod related data.
        model.Environment         = environment; // And what environment are we in.

        // TODO: I still need to debug why I have this here...
        if (environment == GameEnvironment.CLIENT)
        {
            model.SessionID = sessionIDApp; // This is immutable by any layers, the other session ID's are dependent on connections/disconnections and reset accordingly.
        }

        // If we're doing a START event, we need additional information.
        if (cmd == Commands.START)
        {
            // Take the modpack branding info from fmlbranding.properties via Binding. If it's Vanilla see if the config file overwrites it. fmlbranding.properties has priority!
            String modPack = Mercurius.getBinding().getModPack();
            if (modPack == "Vanilla") {
                modPack = Config.modPack;
            }

            // Expand the model
            StatsStartModel start = (StatsStartModel)model;
            start.ClientDateTimeEpoch = System.currentTimeMillis() / 1000L; // Get the current time.
            start.JavaVersion         = System.getProperty("java.version"); // The java version.
            start.JavaAllocatedRAM    = Runtime.getRuntime().totalMemory(); // Memory info
            start.JavaMaxRAM          = Runtime.getRuntime().maxMemory();
            start.MinecraftVersion    = Mercurius.getBinding().getMCVersion(); // Minecraft version
            start.modPack             = modPack; // Mod pack
            start.modPackVersion      = Config.modPackVersion; // and mod pack version.
            this.addAllModData(start); // Add in the mod data.
        }

        // Remove anything the user has opted out of.
        this.data = optOutDataFromModel(model);

        // And upload?
        if (upload)
            this.Upload();
    }

    public void Upload() throws Exception {
        String json = this.toJSON(this.data);
        LogHelper.info(json);
        Upload(json);
    }

    public void Upload(final String json) throws Exception
    {
        // Upload via a new thread because we don't want to lock up the main one.
        Thread newThread = new Thread()
        {
            public void run()
            {
                String ret = Sender.post(json);
                LogHelper.info(ret);
            }
        };
        newThread.setName("MercuriusThread");
        newThread.start();

    }

    // And actually do the HTTP Post with the values
    private static String post(String json)
    {
        try
        {
            String data = "stat=" +  URLEncoder.encode(json, "UTF-8");

            HttpURLConnection conn = (HttpURLConnection)(new URL(StatsConstants.forgeServerUrl)).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", "" + data.getBytes().length);
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(data);
            out.flush();
            out.close();
            BufferedReader in_ = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer ret = new StringBuffer();
            String line;

            while ((line = in_.readLine()) != null)
            {
                ret.append(line);
                ret.append('\r');
            }

            in_.close();
            return ret.toString();
        } catch (UnsupportedEncodingException e) {
            if (Config.spammyLogs) // Let's spam the logs a bit less with errors.
                e.printStackTrace();
                
            return e.toString();
        } catch (IOException e) {
            if (Config.spammyLogs) // Let's spam the logs a bit less with errors.
                e.printStackTrace();

            return e.toString();
        }
    }

    private static Timer timer = new Timer();

    // Start the timer that will send the PING events.
    public void startTimer()
    {
        LogHelper.info("Starting timer...");
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                try
                {
                    Sender.this.collectData(Commands.PING, true);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }, StatsConstants.KEEPALIVETIME, StatsConstants.KEEPALIVETIME);

    }

    public void cancelTimer()
    {
        if (timer != null)
            timer.cancel();
    }

    // Let's remove all the data the user doesn't want to upload to Mercurius
    private StatsPingModel optOutDataFromModel(StatsPingModel model)
    {
        if (Config.OptOut.installID)
            model.InstallID = "";

        if (Config.OptOut.sessionID)
            model.SessionID = "";

        if (model instanceof StatsStartModel)
        {
            StatsStartModel start = (StatsStartModel)model;

            if (Config.OptOut.clientTime)
                start.ClientDateTimeEpoch = 0;

            if (Config.OptOut.environment)
                start.Environment = GameEnvironment.OPTED_OUT;

            if (Config.OptOut.ramAllocated)
                start.JavaAllocatedRAM = 0;

            if (Config.OptOut.ramMax)
                start.JavaMaxRAM = 0;

            if (Config.OptOut.javaVersion)
                start.JavaVersion = "";

            if (Config.OptOut.minecraftVersion)
                start.MinecraftVersion = "";

            if (Config.OptOut.modPack)
                start.modPack = "";

            if (Config.OptOut.modPackVersion)
                start.modPackVersion = "";

            if (Config.OptOut.OS)
                start.OS = "";
        }

        // We iterate through all the mods and if any of them has been opt-ed out, we remove the data.
        Iterator<String> itr = model.Mods.keySet().iterator();
        while (itr.hasNext())
        {
        	String key = itr.next();
            Boolean flag = Config.OptOut.mods.get(key);
            if (flag != null && flag)
                itr.remove();
        }

        return model;
    }

    // Pull all the mod data from the Mod.
    private void addAllModData(StatsStartModel model)
    {
        for (Entry<String, ModInfo> e : Mercurius.getBinding().gatherMods().entrySet())
        {
            String modId = e.getKey();
            ModInfo info = e.getValue();

            Map<String, Object> modData =  model.Mods.get(modId);
            if (modData == null) {
                modData = Maps.newHashMap();
                model.Mods.put(modId, modData);
            }

            modData.put("Version", info.version); // Version of the mod.
            modData.put("Enabled", info.enabled); // Is the mod enabled.
        }
    }
}
