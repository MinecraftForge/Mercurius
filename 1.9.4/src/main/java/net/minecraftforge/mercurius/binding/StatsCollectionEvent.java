package net.minecraftforge.mercurius.binding;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.mercurius.utils.Commands;

public class StatsCollectionEvent extends Event
{
    private final Commands cmd;
    Map<String, Map<String, Object>> data = Maps.newHashMap();

    public StatsCollectionEvent(Commands cmd)
    {
        super();
        this.cmd = cmd;
    }

    public void addEventData(String modId, Map<String, Object> dataDictionary)
    {
        data.put(modId, dataDictionary);
    }

    public Commands getCommand()
    {
        return cmd;
    }
}
