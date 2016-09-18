package net.minecraftforge.mercurius;

import java.util.Map;

import net.minecraftforge.mercurius.utils.Commands;
import net.minecraftforge.mercurius.utils.GameEnvironment;

public interface IMinecraftBinding
{
    boolean isSnooperDisabled();
    GameEnvironment getGameEnvironment();
    String getMCVersion();
    String getModPack();
    String getSessionID();
    void resetSessionID();
    Map<String, Map<String, Object>> gatherModData(Commands cmd);
    Map<String, ModInfo> gatherMods();
    void loadConfig();
}
