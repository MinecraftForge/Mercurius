package net.minecraftforge.mercurius.dataModels;

import net.minecraftforge.mercurius.utils.Commands;
import net.minecraftforge.mercurius.utils.GameEnvironment;

import java.util.Map;

public class StatsPingModel
{
    public Commands cmd;
    public String InstallID;
    public String SessionID;
    public GameEnvironment Environment;
    public Map<String, Map<String, Object>> Mods;
}
