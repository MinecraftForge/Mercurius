package net.minecraftforge.mercurius.dataModels;

// Extends the base Ping model with the relevant information needed for the Start event.
public class StatsStartModel extends StatsPingModel
{
    public long ClientDateTimeEpoch; // Current time on the local mahcine in Epoch
    public String JavaVersion; // Java version
    public long JavaAllocatedRAM; // Java allocated RAM
    public long JavaMaxRAM; // Maximum amount of RAM Java can eat.
    public String MinecraftVersion; // Version of Minecraft
    public String modPack; // What's the current mod pack (if applicable), value loaded from config.
    public String modPackVersion; // What's the current mod pack versoin, value loaded from config.
    public String OS; // Operating system.
}
