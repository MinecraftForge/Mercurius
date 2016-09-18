package net.minecraftforge.mercurius.binding;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraftforge.mercurius.utils.GameEnvironment;
import net.minecraftforge.mercurius.helpers.DataHelper;

import java.io.File;

public class ClientBinding extends CommonBinding
{
    public ClientBinding(File cfgDir)
    {
        super(cfgDir);
    }

    @Override
    public String getSessionID()
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            return sessionIDClient;
        else
            return sessionIDServer;
    }

    @Override
    public void resetSessionID() { sessionIDServer = DataHelper.CreateID(); }

    @Override
    public boolean isSnooperDisabled()
    {
        return !Minecraft.getMinecraft().isSnooperEnabled();
    }

    @Override
    public GameEnvironment getGameEnvironment()
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            return GameEnvironment.CLIENT;
        else
            return GameEnvironment.SERVER_LOCAL;
    }
}
