package net.minecraftforge.stats.proxies;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.stats.helpers.StatsConstants;
import net.minecraftforge.stats.helpers.DataHelper;

import java.io.File;

/**
 * Created by tamas on 7/15/16.
 */
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {
        Configuration config = new Configuration(e.getSuggestedConfigurationFile());
        Configuration config_local = new Configuration(
                new File(e.getModConfigurationDirectory().getParentFile(), "/local/local_forgestats.cfg"));

        config.load();
        config_local.load();

        StatsConstants.InstallID = DataHelper.CreateID();
        StatsConstants.SessionID = DataHelper.CreateID();

        StatsConstants.modPack = config
                .get(Configuration.CATEGORY_GENERAL, "modPack", StatsConstants.modPack).getString();

        StatsConstants.spammyLogs = config
                .get(Configuration.CATEGORY_GENERAL, "spammyLogs", StatsConstants.spammyLogs).getBoolean();

		/*
		 * Local configs
		 */

        StatsConstants.InstallID = config_local
                .get(Configuration.CATEGORY_GENERAL, "InstallId", StatsConstants.InstallID).getString();

		/*
		 * Init local dummy config to show analytics values
		 */

        StatsConstants.dataConfig = new Configuration(
                new File(e.getModConfigurationDirectory().getParentFile(), "/local/local_forgestats_data.cfg"));

        config.save();
        config_local.save();

    }

    public void init(FMLInitializationEvent e) {

    }

    public void postInit(FMLPostInitializationEvent e) {

    }
}
