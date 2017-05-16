package net.minecraftforge.mercurius;

import net.minecraftforge.mercurius.helpers.DataHelper;

// Global config that's persisted across mod pack updates. This is to ensure installID doesn't get regenerated in case a mod pack gets updated.
public class GlobalConfig
{
    public static String installID = DataHelper.CreateID();
}
