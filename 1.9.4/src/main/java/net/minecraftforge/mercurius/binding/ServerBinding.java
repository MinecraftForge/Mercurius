package net.minecraftforge.mercurius.binding;

import java.io.File;

import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.mercurius.utils.GameEnvironment;
import net.minecraftforge.mercurius.helpers.DataHelper;

public class ServerBinding extends CommonBinding
{
    public ServerBinding(File cfgDir)
    {
        super(cfgDir);
    }

    @Override
    public String getSessionID() { return sessionIDServer; }

    @Override
    public void resetSessionID() { sessionIDServer = DataHelper.CreateID(); }

    @Override
    public boolean isSnooperDisabled()
    {
        return !((DedicatedServer)FMLCommonHandler.instance().getMinecraftServerInstance()).isSnooperEnabled();
    }

    @Override
    public GameEnvironment getGameEnvironment()
    {
        return GameEnvironment.SERVER_DEDICATED;
    }
}
