package net.minecraftforge.mercurius.dataModels;

import net.minecraftforge.mercurius.utils.Commands;
import net.minecraftforge.mercurius.utils.GameEnvironment;

import java.util.Map;

// Generic model that's the base for all data models in Mercurius. This model is sent during Ping and Stop events.
public class StatsPingModel
{
    public Commands cmd; // Type of analytics event. E.g. Start, stop, ping, crash.
    public String InstallID; // Unique identifer per install. Used to track multiple start and stops of a client/server
    public String SessionID; // Unique identifer per world start. This is regenerated every time you start/stop a world.
    public String ClientSessionID; // Unique identifier per game start. This is regenerated every time you start Minecraft.
    public GameEnvironment Environment; // Defines server vs client in the JSON.
    public Map<String, Map<String, Object>> Mods; // Map of mod provided information. Provides information to Mercurius on what mods and their versions are installed. This is also where API values are sent.
}
