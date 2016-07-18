package net.minecraftforge.stats.proxies;

import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.stats.StatsMod;
import net.minecraftforge.stats.utils.Commands;

/**
 * Created by tamas on 7/15/16.
 */
public class ServerProxy extends CommonProxy {
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
