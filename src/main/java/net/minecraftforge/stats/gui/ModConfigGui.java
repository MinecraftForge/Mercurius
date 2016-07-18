package net.minecraftforge.stats.gui;

import java.util.List;

import com.google.gson.Gson;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.stats.StatsMod;
import net.minecraftforge.stats.dataModels.StatsStartModel;
import net.minecraftforge.stats.helpers.StatsConstants;
import net.minecraftforge.stats.utils.Commands;

public class ModConfigGui extends GuiConfig {

    public ModConfigGui(GuiScreen guiScreen) {
        super(guiScreen, ModConfigGui.getConfigElements(), StatsMod.MODID, false, false,
                "Analytics event data");
    }

    public static List<IConfigElement> getConfigElements() {

        if(StatsMod.sender.IsSnooperDisabled()) {
            StatsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, "Snooper", "Off");
            return new ConfigElement(StatsConstants.dataConfig.getCategory(Configuration.CATEGORY_GENERAL))
                    .getChildElements();
        }

        try {
            StatsMod.sender.CollectData(Commands.START, false); // collect a START event, but don't upload it.
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(StatsMod.sender.data == null) {
            StatsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, "Collection failed", "ooops...");
            return new ConfigElement(StatsConstants.dataConfig.getCategory(Configuration.CATEGORY_GENERAL))
                    .getChildElements();
        }

        StatsStartModel model = (StatsStartModel)StatsMod.sender.data;

        StatsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, "InstallID", model.InstallID);
        StatsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, "InstallID_OptOut", false);

        StatsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, "SessionID", model.SessionID);
        StatsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, "SessionID_OptOut", false);

        StatsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, "ClientDateTimeEpoch", model.ClientDateTimeEpoch);
        StatsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, "ClientDateTimeEpoch_OptOut", false);

        StatsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, "JavaAllocatedRAM", model.JavaAllocatedRAM);
        StatsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, "JavaAllocatedRAM_OptOut", false);

        StatsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, "JavaMaxRAM", model.JavaMaxRAM);
        StatsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, "JavaMaxRAM_OptOut", false);

        StatsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, "JavaVersion", model.JavaVersion);
        StatsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, "JavaVersion_OptOut", false);

        StatsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, "MinecraftVersion", model.MinecraftVersion);
        StatsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, "MinecraftVersion_OptOut", false);

        StatsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, "Environment", model.Environment.toString());
        StatsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, "Environment_OptOut", false);

        StatsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, "modPack", model.modPack);
        StatsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, "modPack_OptOut", false);

        Gson json = new Gson();

        for(String key : model.Mods.keySet()) {
            StatsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, key, json.toJson(model.Mods.get(key)));
            StatsConstants.dataConfig.get(Configuration.CATEGORY_GENERAL, key + "_OptOut", false);
        }


        StatsConstants.dataConfig.save();
        return new ConfigElement(StatsConstants.dataConfig.getCategory(Configuration.CATEGORY_GENERAL))
                .getChildElements();
    }
}