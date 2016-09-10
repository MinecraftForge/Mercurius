package net.minecraftforge.mercurius;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraftforge.mercurius.helpers.DataHelper;

public class Config
{
    public static boolean spammyLogs = false;
    public static String modPack = "Vanilla";
    public static OptOuts OptOut = new OptOuts();

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
