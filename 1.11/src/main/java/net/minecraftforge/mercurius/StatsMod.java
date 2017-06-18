package net.minecraftforge.mercurius;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.mercurius.binding.ClientBinding;
import net.minecraftforge.mercurius.binding.ModConfigGui;
import net.minecraftforge.mercurius.binding.ServerBinding;
import net.minecraftforge.mercurius.utils.Commands;
import net.minecraftforge.mercurius.utils.GameEnvironment;

@Mod(modid = StatsMod.MODID, name = StatsMod.MODNAME,  version = StatsMod.VERSION, guiFactory = StatsMod.GUIFACTORY)
public class StatsMod
{
    public static final String MODID = "mercurius";
    public static final String MODNAME = "Mercurius";
    public static final String VERSION = "1.0.6"; //Can we get gradle to replace these things?
    public static final String GUIFACTORY = "net.minecraftforge.mercurius.StatsMod$GuiFactory";

    @Mod.Instance(StatsMod.MODID)
    public static StatsMod instance;
    public static IMinecraftBinding binding;

    @EventHandler
    public void preInit(FMLPreInitializationEvent e)
    {
        // Init the binding
        if (e.getSide() == Side.CLIENT)
            binding = new ClientBinding(e.getModConfigurationDirectory());
        else
            binding = new ServerBinding(e.getModConfigurationDirectory());
        Mercurius.bootstrap(binding);
    }

    @EventHandler
    public void init(FMLInitializationEvent e)
    {
        FMLCommonHandler.instance().bus().register(new ConnectionEvents());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e)
    {
        try
        {
          // TODO: Do we want to tick when people are sitting in menu?
          // TODO: We also need to figure out how to deal with ticking the client in MP? Because the cancel in server stop would derp it up.
            Mercurius.getSender().collectData(Commands.START, true); // We started so let's say START to Mercurius and upload it.
            Mercurius.getSender().startTimer();
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent e)
    {
        try
        {
            Mercurius.getSender().collectData(Commands.START, true); // Server has started, let's send START to Mercurius.
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
    }

    @EventHandler
    public void serverStopping(FMLServerStoppingEvent e)
    {
        try
        {
            Mercurius.getSender().collectData(Commands.STOP, true); // Send STOP for server.
            Mercurius.getSender().cancelTimer(); // Cancel the PING timer.
            Mercurius.getBinding().resetSessionID(); // And reset the ID for the server. This is needed so we can differentiate between start/stop of world without restarting the game itself.
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
    }

    public static class GuiFactory implements IModGuiFactory
    {
        @Override public Class<? extends GuiScreen> mainConfigGuiClass(){ return ModConfigGui.class; }
        @Override public void initialize(Minecraft minecraftInstance) {}
        @Override public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() { return null; }
        @Override public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) { return null; }
    }

    public class ConnectionEvents
    {
        @SubscribeEvent
        public void onConnectionOpened(FMLNetworkEvent.ClientConnectedToServerEvent e)
        {
            try
            {
                if(!e.isLocal()) // only fire on actual MP servers not on local.
                {
                    // Let's trigger a START on the remote server when someone joins the server.
                    Mercurius.getSender().collectData(Commands.START, true, GameEnvironment.SERVER_NON_LOCAL);
                }
            }
            catch (Exception e1)
            {
                e1.printStackTrace();
            }
        }

        @SubscribeEvent
        public void disconnectedFromServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent e)
        {
            try
            {
                if(!e.getManager().isLocalChannel()) // only fire on actual MP servers not on local.
                {
                    // Let's trigger a STOP on the remote server when someones leaves the server.
                    Mercurius.getSender().collectData(Commands.STOP, true, GameEnvironment.SERVER_NON_LOCAL);
                    Mercurius.getBinding().resetSessionID(); // Don't forget to reset ID's.
                }
            }
            catch (Exception e1)
            {
                e1.printStackTrace();
            }
        }
    }
}
