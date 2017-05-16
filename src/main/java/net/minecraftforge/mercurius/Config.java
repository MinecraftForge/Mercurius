package net.minecraftforge.mercurius;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraftforge.mercurius.helpers.DataHelper;

public class Config
{
    // If enabled will spam the log files with Mercurius events and raw outputs of the JSON.
    public static boolean spammyLogs = false;
    public static String modPack = "Vanilla";
    public static String modPackVersion = "1.0";
    public static OptOuts OptOut = new OptOuts();

    // Class that stores in the config file, what fields a user has opted out from sending to Mercurius
    public static class OptOuts
    {
        public boolean installID = false;
        public boolean sessionID = false;
        public boolean clientTime = false;
        public boolean environment = false;
        public boolean ramAllocated = false;
        public boolean ramMax = false;
        public boolean javaVersion = false;
        public boolean minecraftVersion = false;
        public boolean modPack = false;
        public Map<String, Boolean> mods = Maps.newHashMap();
    }
}
