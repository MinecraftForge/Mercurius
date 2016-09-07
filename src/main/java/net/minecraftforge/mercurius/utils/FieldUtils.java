package net.minecraftforge.mercurius.utils;

import java.lang.reflect.Field;
///Lex Note: GOD I hate this class.... Need better system...
public class FieldUtils
{
    public static boolean getBoolean(Object instance, Field f)
    {
        try {
            return f.getBoolean(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static int getInt(Object instance, Field f)
    {
        try {
            return f.getInt(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static long getLong(Object instance, Field f)
    {
        try {
            return f.getLong(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static Object getObject(Object instance, Field f)
    {
        try {
            return f.get(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void set(Object instance, Field f, Object v)
    {
        try {
            f.set(instance, v);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static byte getByte(Object instance, Field f)
    {
        try {
            return f.getByte(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static char getChar(Object instance, Field f)
    {
        try {
            return f.getChar(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static double getDouble(Object instance, Field f)
    {
        try {
            return f.getDouble(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static float getFloat(Object instance, Field f)
    {
        try {
            return f.getFloat(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static short getShort(Object instance, Field f)
    {
        try {
            return f.getShort(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
