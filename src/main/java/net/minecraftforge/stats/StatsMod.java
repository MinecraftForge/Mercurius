package net.minecraftforge.stats;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.stats.proxies.CommonProxy;
import net.minecraftforge.stats.utils.Commands;
import net.minecraftforge.stats.utils.sender.CommonSender;


@Mod(modid = StatsMod.MODID, name = StatsMod.MODNAME,  version = StatsMod.VERSION, guiFactory = StatsMod.GUIFACTORY)
public class StatsMod
{
    public static final String MODID = "forgestats";
    public static final String MODNAME = "Forge Stats";
    public static final String VERSION = "1.0.0";
    public static final String GUIFACTORY = "net.minecraftforge.stats.gui.GuiFactory";

    @Mod.Instance(StatsMod.MODID)
    public static StatsMod instance;

    @SidedProxy(clientSide = "net.minecraftforge.stats.proxies.ClientProxy", serverSide = "net.minecraftforge.stats.proxiess.ServerProxy")
    public static CommonProxy proxy;

    @SidedProxy(clientSide = "net.minecraftforge.stats.utils.sender.ClientSender", serverSide = "net.minecraftforge.stats.utils.sender.ServerSender")
    public static CommonSender sender;

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        // logger = e.getModLog();
        proxy.preInit(e);
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent e) {
        try {
            sender.CollectData(Commands.START);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @EventHandler
    public void serverStopping(FMLServerStoppingEvent e) {
        try {
            sender.CollectData(Commands.STOP);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        sender.CancelTimer();
    }

}
