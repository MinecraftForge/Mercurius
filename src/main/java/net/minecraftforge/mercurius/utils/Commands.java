package net.minecraftforge.mercurius.utils;

import com.google.common.base.Throwables;

import net.minecraftforge.mercurius.dataModels.*;

public enum Commands
{
    START(StatsStartModel.class),
    PING(StatsPingModel.class),
    STOP(StatsPingModel.class),
    CRASH(StatsPingModel.class); // Future use, once we have all MC versions done.

    private Class<? extends StatsPingModel> cls;
    private Commands(Class<? extends StatsPingModel> cls)
    {
        this.cls = cls;
    }

    public StatsPingModel newInstance()
    {
        StatsPingModel ret;
        try {
            ret = this.cls.newInstance();
            ret.cmd = this;
            return ret;
        } catch (Exception e) {
            Throwables.propagate(e);
        }
        return null; // Shouldn't happen
    }
}
