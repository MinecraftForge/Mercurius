package net.minecraftforge.stats.utils.sender;

import net.minecraft.client.Minecraft;

/**
 * Created by tamas on 7/15/16.
 */
public class ClientSender extends CommonSender {

    @Override
    public boolean IsSnooperDisabled() {
        if (Minecraft.getMinecraft().isSnooperEnabled()) {
            return false;
        }
        return true;
    }
}
