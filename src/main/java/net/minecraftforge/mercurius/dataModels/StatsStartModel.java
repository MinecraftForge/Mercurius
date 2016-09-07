package net.minecraftforge.mercurius.dataModels;

import net.minecraftforge.mercurius.utils.GameEnvironment;

public class StatsStartModel extends StatsPingModel
{
    public long ClientDateTimeEpoch;
    public String JavaVersion;
    public long JavaAllocatedRAM;
    public long JavaMaxRAM;
    public String MinecraftVersion;
    public GameEnvironment Environment;
    public String modPack;
}
