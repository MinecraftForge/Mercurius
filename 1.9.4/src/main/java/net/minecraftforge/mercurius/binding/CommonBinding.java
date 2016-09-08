package net.minecraftforge.mercurius.binding;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.mercurius.Config;
import net.minecraftforge.mercurius.GlobalConfig;
import net.minecraftforge.mercurius.IMinecraftBinding;
import net.minecraftforge.mercurius.ModInfo;
import net.minecraftforge.mercurius.StatsMod;
import net.minecraftforge.mercurius.utils.Commands;
import net.minecraftforge.mercurius.utils.FieldUtils;

abstract class CommonBinding implements IMinecraftBinding
{
    protected File cfgDir;
    Configuration global = null;
    Configuration local = null;

    protected CommonBinding(File cfgDir)
    {
        this.cfgDir = cfgDir;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public String getMCVersion()
    {
        return Loader.instance().getMCVersionString();
    }

    @Override
    public String getModPack()
    {
        String branding = Loader.instance().getFMLBrandingProperties().get("fmlbranding");
        return branding == null ? "Vanilla" : branding;
    }

    @Override
    public Map<String, Map<String, Object>> gatherModData(Commands cmd)
    {
        StatsCollectionEvent event = new StatsCollectionEvent(cmd);
        MinecraftForge.EVENT_BUS.post(event);
        return event.data;
    }

    @Override
    public Map<String, ModInfo> gatherMods()
    {
        Map<String, ModInfo> mods = Maps.newHashMap();
        for (ModContainer mod : Loader.instance().getModList())
        {
            ModInfo info = new ModInfo();
            info.version = mod.getVersion();
            info.enabled = Loader.instance().getActiveModList().contains(mod);
            mods.put(mod.getModId(), info);
        }
        return mods;
    }

    public void onConfigChange(ConfigChangedEvent e)
    {
    	if (e.getModID().equals(StatsMod.MODID))
    		loadConfig();
    }

    @Override
    public void loadConfig()
    {
        global = createConfig(new File(cfgDir.getParent(), "/local/local_mercurius.cfg"), GlobalConfig.class);
        local = createConfig(new File(cfgDir, "/" + StatsMod.MODNAME + ".cfg"), Config.class);
        global.save();
        local.save();
    }

    private Configuration createConfig(File file, Class<?> cls)
    {
        Configuration cfg = new Configuration(file);
        cfg.load();
        String category = "general";
        for (Field f : cls.getDeclaredFields())
        {
            if (!Modifier.isStatic(f.getModifiers()) || !Modifier.isPublic(f.getModifiers()))
                continue;

            createConfig(category, cfg, f.getType(), f, null);
        }
        return cfg;
    }

    // TODO: Move to a different spot maybe? A lot of code here.

    @SuppressWarnings("unchecked")
	private void createConfig(String category, Configuration cfg, Class<?> ftype, Field f, Object instance)
    {
        Property prop = null;
        String comment = null; // TODO Take From Annotation
        String langKey = StatsMod.MODID + "." + category + "." + f.getName().toLowerCase(Locale.ENGLISH); //TODO Take From Annotation
        int min = -1; // TODO Take From Annotation
        int max = -1; // TODO Take From Annotation

        if (ftype == boolean.class)
        {
            prop = cfg.get(category, f.getName(), FieldUtils.getBoolean(instance, f), comment);
            FieldUtils.set(instance, f, prop.getBoolean());
        }
        else if (ftype == boolean[].class)
        {
            prop = cfg.get(category, f.getName(), (boolean[])FieldUtils.getObject(instance, f), comment);
            FieldUtils.set(instance, f, prop.getBooleanList());
        }
        else if (ftype == byte.class)
        {
            prop = cfg.get(category, f.getName(), FieldUtils.getByte(instance, f), comment, Byte.MIN_VALUE, Byte.MAX_VALUE);
            FieldUtils.set(instance, f, (byte)prop.getInt());
        }
        else if (ftype == char.class)
        {
            prop = cfg.get(category, f.getName(), FieldUtils.getChar(instance, f), comment, Character.MIN_VALUE, Character.MAX_VALUE);
            FieldUtils.set(instance, f, (char)prop.getInt());
        }
        else if (ftype == double.class)
        {
            prop = cfg.get(category, f.getName(), FieldUtils.getDouble(instance, f), comment, Double.MIN_VALUE, Double.MAX_VALUE);
            FieldUtils.set(instance, f, prop.getDouble());
        }
        else if (ftype == float.class)
        {
            prop = cfg.get(category, f.getName(), FieldUtils.getFloat(instance, f), comment, Float.MIN_VALUE, Float.MAX_VALUE);
            FieldUtils.set(instance, f, (float)prop.getDouble());
        }
        else if (ftype == int.class)
        {
            prop = cfg.get(category, f.getName(), FieldUtils.getInt(instance, f), comment, Integer.MIN_VALUE, Integer.MAX_VALUE);
            FieldUtils.set(instance, f, prop.getInt());
        }
        else if (ftype == long.class)
        {
            prop = cfg.get(category, f.getName(), FieldUtils.getLong(instance, f), comment, Long.MIN_VALUE, Long.MAX_VALUE);
            FieldUtils.set(instance, f, prop.getLong());
        }
        else if (ftype == short.class)
        {
            prop = cfg.get(category, f.getName(), FieldUtils.getShort(instance, f), comment, Short.MIN_VALUE, Short.MAX_VALUE);
            FieldUtils.set(instance, f, (short)prop.getInt());
        }
        else if (ftype == String.class)
        {
            prop = cfg.get(category, f.getName(), (String)FieldUtils.getObject(instance, f), comment);
            FieldUtils.set(instance, f, prop.getString());
        }
        else if (ftype == Map.class)
        {
        	String sub = category + "." + f.getName().toLowerCase(Locale.ENGLISH);
        	Map<String, Object> m = (Map<String, Object>)FieldUtils.getObject(instance, f);
            ParameterizedType type = (ParameterizedType)f.getGenericType();
            Type mtype = type.getActualTypeArguments()[1];

            cfg.getCategory(sub).setComment(comment);

        	for (Entry<String, Object> e : m.entrySet())
        	{
                if (mtype == Boolean.class)
                    prop = cfg.get(sub, e.getKey(), (Boolean)e.getValue(), null);
                else if (mtype == boolean[].class)
                    prop = cfg.get(sub, e.getKey(), (boolean[])e.getValue(), null);
                else if (mtype == Byte.class)
                    prop = cfg.get(sub, e.getKey(), (Byte)e.getValue(), null, Byte.MIN_VALUE, Byte.MAX_VALUE);
                else if (mtype == Character.class)
                    prop = cfg.get(sub, e.getKey(), (Character)e.getValue(), null, Character.MIN_VALUE, Character.MAX_VALUE);
                else if (mtype == Double.class)
                    prop = cfg.get(sub, e.getKey(), (Double)e.getValue(), null, Double.MIN_VALUE, Double.MAX_VALUE);
                else if (mtype == Float.class)
                    prop = cfg.get(sub, e.getKey(), (Float)e.getValue(), null, Float.MIN_VALUE, Float.MAX_VALUE);
                else if (mtype == Integer.class)
                    prop = cfg.get(sub, e.getKey(), (Integer)e.getValue(), null, Integer.MIN_VALUE, Integer.MAX_VALUE);
                else if (mtype == Long.class)
                    prop = cfg.get(sub, e.getKey(), (Long)e.getValue(), null, Long.MIN_VALUE, Long.MAX_VALUE);
                else if (mtype == Short.class)
                    prop = cfg.get(sub, e.getKey(), (Short)e.getValue(), null, Short.MIN_VALUE, Short.MAX_VALUE);
                else if (mtype == String.class)
                    prop = cfg.get(sub, e.getKey(), (String)FieldUtils.getObject(instance, f), null);
                else
                    throw new RuntimeException("Unknown type in map! " + f.getDeclaringClass() + "/" + f.getName() + " " + mtype);

                prop.setLanguageKey(langKey + "." + e.getKey().toLowerCase(Locale.ENGLISH));
        	}
            prop = null;
        }
        // TODO test other stuff
        else if (ftype.getSuperclass() == Object.class) //Only support classes that are one level below Object.
        {
        	String sub = category + "." + f.getName().toLowerCase(Locale.ENGLISH);
        	Object sinst = FieldUtils.getObject(instance, f);
            for (Field sf : ftype.getDeclaredFields())
            {
                if (!Modifier.isPublic(sf.getModifiers()))
                    continue;

                createConfig(sub, cfg, sf.getType(), sf, sinst);
            }
        }
        else
            throw new RuntimeException("Unknown type in config! " + f.getDeclaringClass() + "/" + f.getName() + " " + ftype);

        if (prop != null)
        {
	        prop.setLanguageKey(langKey);
	        if (min != -1)
	            prop.setMinValue(min);
	        if (max != -1)
	            prop.setMaxValue(max);
        }
    }


}
