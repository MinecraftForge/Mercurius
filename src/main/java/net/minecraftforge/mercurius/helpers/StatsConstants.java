package net.minecraftforge.mercurius.helpers;

public class StatsConstants
{
    // API URL.
    public static String forgeServerUrl = "http://stats.minecraftforge.net/api/v2/";

    // Amount of times to hash the created ID's. The more the better.
    public static int HASHCOUNT = 5;

    // Amount of time between PING events.
    public static int KEEPALIVETIME = 5 * 60 * 1000; // 5 minutes.
}
