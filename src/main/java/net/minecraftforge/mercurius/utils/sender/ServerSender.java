package net.minecraftforge.mercurius.utils.sender;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * Created by tamas on 7/15/16.
 */
public class ServerSender extends CommonSender{
    @Override
    public boolean IsSnooperDisabled() {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server instanceof DedicatedServer) {
            DedicatedServer ds = (DedicatedServer) server;
            if (ds.isSnooperEnabled()) {
                return false;
            }
            return true;
        }
        return false;
    }


}
