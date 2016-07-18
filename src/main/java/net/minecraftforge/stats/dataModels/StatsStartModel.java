package net.minecraftforge.stats.dataModels;

import net.minecraftforge.stats.utils.GameEnvironment;

/**
 * Created by tamas on 7/15/16.
 */
public class StatsStartModel extends StatsPingModel {

    public StatsStartModel() {
        super();
    }

    public StatsStartModel(StatsPingModel prevModel) {
        this.Mods = prevModel.Mods;
        this.InstallID = prevModel.InstallID;
        this.SessionID = prevModel.SessionID;
        this.cmd = prevModel.cmd;
    }

    public long ClientDateTimeEpoch;
    public String JavaVersion;
    public long JavaAllocatedRAM;
    public long JavaMaxRAM;
    public String MinecraftVersion;
    public GameEnvironment Environment;
    public String modPack;
}
