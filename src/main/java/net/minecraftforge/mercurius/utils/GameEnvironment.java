package net.minecraftforge.mercurius.utils;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by tamas on 7/15/16.
 */
public enum GameEnvironment {
    SERVER_LOCAL,
    SERVER_DEDICATED,
    CLIENT,
    OPTED_OUT;

    public static GameEnvironment getEnvironment() {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            return GameEnvironment.CLIENT;
        else {
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            if (server.isDedicatedServer()) {
                return GameEnvironment.SERVER_DEDICATED;
            }
            return GameEnvironment.SERVER_LOCAL;
        }
    }
}
