package net.minecraftforge.mercurius.helpers;

import net.minecraftforge.common.config.Configuration;

/**
 * Created by tamas on 7/15/16.
 */
public class StatsConstants {

    public static String forgeServerUrl = "http://mercurius.minecraftforge.net/api/mercurius/";

    public static String InstallID = "";
    public static String SessionID = "";

    public static int HASHCOUNT = 5;
    public static int KEEPALIVETIME = 5 * 60 * 1000; // 5 minutes.

    public static String modPack = "Vanilla";

    public static Configuration dataConfig = null;

    public static boolean spammyLogs = false;

}