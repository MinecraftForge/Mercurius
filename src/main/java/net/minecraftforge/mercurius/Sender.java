package net.minecraftforge.mercurius;

import com.google.common.collect.Maps;
import com.google.gson.Gson;

import net.minecraftforge.mercurius.dataModels.StatsPingModel;
import net.minecraftforge.mercurius.dataModels.StatsStartModel;
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

public class Sender
{
    public StatsPingModel data;

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
        if (this.isSnooperDisabled())
        {
            LogHelper.info("Snooper is disabled... aborting collection.");
            return;
        }

        LogHelper.info("Starting collecting data for event "+cmd.toString());

        StatsPingModel model = cmd.newInstance();
        model.InstallID = GlobalConfig.installID;
        model.SessionID = Mercurius.getBinding().getSessionID();
        model.Mods = Mercurius.getBinding().gatherModData(cmd);

        if (cmd == Commands.START)
        {
            StatsStartModel start = (StatsStartModel)model;
            start.ClientDateTimeEpoch = System.currentTimeMillis() / 1000L;
            start.JavaVersion         = System.getProperty("java.version");
            start.JavaAllocatedRAM    = Runtime.getRuntime().totalMemory();
            start.JavaMaxRAM          = Runtime.getRuntime().maxMemory();
            start.MinecraftVersion    = Mercurius.getBinding().getMCVersion();
            start.modPack             = Mercurius.getBinding().getModPack();;
            start.Environment         = Mercurius.getBinding().getGameEnvironment();
            this.addAllModData(start);
        }

        this.data = optOutDataFromModel(model);
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
            // TODO Auto-generated catch block
            e.printStackTrace();
            return e.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return e.toString();
        }
    }

    private static Timer timer = new Timer();

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
        }

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

            modData.put("Version", info.version);
            modData.put("Enabled", info.enabled);
        }
    }
}
