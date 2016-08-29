package net.minecraftforge.mercurius.proxies;

import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.mercurius.StatsMod;
import net.minecraftforge.mercurius.utils.Commands;

/**
 * Created by tamas on 7/15/16.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);

        StatsMod.sender.StartTimer();
        try {
            StatsMod.sender.CollectData(Commands.START);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }
}
