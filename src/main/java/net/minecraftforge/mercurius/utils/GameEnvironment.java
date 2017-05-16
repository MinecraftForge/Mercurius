package net.minecraftforge.mercurius.utils;

public enum GameEnvironment
{
    SERVER_LOCAL, // The local integrated server
    SERVER_NON_LOCAL, // playing on an MP server
    SERVER_DEDICATED, // the dedicated server app.
    CLIENT,    // The Minecraft app itself.
    OPTED_OUT; // Not in use currently.
}
