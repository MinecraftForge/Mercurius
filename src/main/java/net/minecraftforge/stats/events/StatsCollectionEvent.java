package net.minecraftforge.stats.events;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.stats.utils.Commands;
import net.minecraftforge.stats.utils.GameEnvironment;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by tamas on 7/15/16.
 */
public class StatsCollectionEvent extends Event {

    public final Commands cmd;

    public static Hashtable<String, Hashtable<String, Object>> modProvidedData;

    public StatsCollectionEvent(Commands cmd) {
        super();
        this.cmd = cmd;
        StatsCollectionEvent.modProvidedData = new Hashtable<String,Hashtable<String, Object>>();
    }

    public void addEventData(String modId, Hashtable<String, Object> dataDictionary) {
        StatsCollectionEvent.modProvidedData.put(modId, dataDictionary);
    }
}
