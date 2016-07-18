package net.minecraftforge.stats.dataModels;

import net.minecraftforge.stats.utils.Commands;

import java.util.Hashtable;

/**
 * Created by tamas on 7/18/16.
 */
public class StatsPingModel {

    public Commands cmd;
    public String InstallID;
    public String SessionID;

    public Hashtable<String, Hashtable<String, Object>> Mods;
}
