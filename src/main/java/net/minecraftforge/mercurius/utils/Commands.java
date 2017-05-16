package net.minecraftforge.mercurius.utils;

import com.google.common.base.Throwables;

import net.minecraftforge.mercurius.dataModels.*;

public enum Commands
{
    START(StatsStartModel.class), // Sent during start of a client/server.
    PING(StatsPingModel.class), // Sent during the ping event that's triggered every 5 mintues (configurable)
    STOP(StatsPingModel.class), // Sent during the stop of a client/server.
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
