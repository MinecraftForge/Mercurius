package net.minecraftforge.mercurius.binding;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.mercurius.utils.GameEnvironment;

public class ClientBinding extends CommonBinding
{
    public ClientBinding(File cfgDir)
    {
        super(cfgDir);
    }

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
