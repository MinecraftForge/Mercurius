package net.minecraftforge.mercurius.dataModels;

import net.minecraftforge.mercurius.utils.Commands;

import java.util.Map;

public class StatsPingModel
{
    public Commands cmd;
    public String InstallID;
    public String SessionID;

    public Map<String, Map<String, Object>> Mods;
}
