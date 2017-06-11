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
    public static final String VERSION = "1.0.5"; //Can we get gradle to replace these things?
    public static final String GUIFACTORY = "net.minecraftforge.mercurius.StatsMod$GuiFactory";

    @Mod.Instance(StatsMod.MODID)
    public static StatsMod instance;
    public static IMinecraftBinding binding;

    @EventHandler
    public void preInit(FMLPreInitializationEvent e)
    {
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
            Mercurius.getSender().collectData(Commands.START, true);
            Mercurius.getSender().startTimer(); //Do we want to tick when people are sitting in menu?
            //We also need to figure out how to deal with ticking the client in MP? Because the cancel in server stop would derp it up.
        }
        catch (Exception e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent e)
    {
        try
        {
            Mercurius.getSender().collectData(Commands.START, true);
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
            Mercurius.getSender().collectData(Commands.STOP, true);
            Mercurius.getSender().cancelTimer();
            Mercurius.getBinding().resetSessionID();
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
        @Override public GuiScreen createConfigGui(GuiScreen parentScreen) { return new ModConfigGui(parentScreen); }
        @Override public boolean hasConfigGui() { return true; }
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
                    Mercurius.getSender().collectData(Commands.STOP, true, GameEnvironment.SERVER_NON_LOCAL);
                    Mercurius.getBinding().resetSessionID();
                }
            }
            catch (Exception e1)
            {
                e1.printStackTrace();
            }
        }
    }
}
