package net.minecraftforge.mercurius;

public class Mercurius
{
    private static IMinecraftBinding binding = null;
    private static Sender sender = new Sender();

    public static void bootstrap(IMinecraftBinding binding)
    {
        if (Mercurius.binding != null)
            throw new IllegalStateException("Can not bootstrap Mercurius twice!");

        Mercurius.binding = binding;

        for (String modID : Mercurius.getBinding().gatherMods().keySet())
        {
        	Config.OptOut.mods.put(modID, false);
        }

        binding.loadConfig();
    }

    public static IMinecraftBinding getBinding()
    {
        return binding;
    }

    public static Sender getSender()
    {
        return sender;
    }
}
