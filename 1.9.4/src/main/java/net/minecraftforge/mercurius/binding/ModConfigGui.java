package net.minecraftforge.mercurius.binding;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import static net.minecraftforge.common.config.Configuration.*;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.mercurius.Mercurius;
import net.minecraftforge.mercurius.StatsMod;
import net.minecraftforge.mercurius.utils.Commands;

public class ModConfigGui extends GuiConfig
{
    public ModConfigGui(GuiScreen guiScreen)
    {
        super(guiScreen, getConfigElements(), StatsMod.MODID, false, false, "Analytics event data");
    }

    public static List<IConfigElement> getConfigElements()
    {
    	CommonBinding bind = (CommonBinding)Mercurius.getBinding();
        if(Mercurius.getSender().isSnooperDisabled())
        {
        	bind.local.get(Configuration.CATEGORY_GENERAL, "Snooper", "Off");
            return new ConfigElement(bind.local.getCategory(CATEGORY_GENERAL)).getChildElements();
        }

        try
        {
            Mercurius.getSender().collectData(Commands.START, false); // collect a START event, but don't upload it.
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        /*
        if(StatsMod.sender.data == null) {
            StatsConstants.dataConfig.get(Config.CATEGORY_GENERAL, "Collection failed", "ooops...");
            return new ConfigElement(StatsConstants.dataConfig.getCategory(Config.CATEGORY_GENERAL))
                    .getChildElements();
        }

        StatsPingModel model = (StatsPingModel)Mercurius.getSender().data;

        //Gson json = new Gson();

        for (String key : model.Mods.keySet())
        {
            //StatsConstants.dataConfig.get(Config.CATEGORY_GENERAL, key, json.toJson(model.Mods.get(key)));
        }

        StatsConstants.dataConfig.save();

        */
        List<IConfigElement> ret = new ConfigElement(bind.global.getCategory(CATEGORY_GENERAL)).getChildElements();
        ret.addAll(new ConfigElement(bind.local.getCategory(CATEGORY_GENERAL)).listCategoriesFirst(false).getChildElements());
        return ret;
    }
}