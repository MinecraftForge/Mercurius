package net.minecraftforge.mercurius.utils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import com.google.common.annotations.Beta;

public class LauncherCachingHack
{
	private static final int MAX_AGE = 24 * 60 * 60 * 1000; // 24 Hours
	public static void cullChecksum()
	{
		try
		{
			File dir = getJar();
			File sha = new File(dir.getAbsolutePath() + ".sha");
			if (sha.exists() && sha.isFile() && sha.lastModified() < System.currentTimeMillis() - MAX_AGE)
				sha.delete(); // Delete it, so that next time the launcher starts, it'll grab the new one from Maven.
		}
		catch (Exception e)
		{
			LogHelper.error("[Mercurius] Could not find/delete checksum file. Mercurius may not update!");
			e.printStackTrace();
		}
	}


	private static File getJar() throws Exception
	{
	    URL url = LauncherCachingHack.class.getProtectionDomain().getCodeSource().getLocation();
	    String extURL = url.toExternalForm();

	    if (!extURL.endsWith(".jar"))
	    {
	        String suffix = "/" + (LauncherCachingHack.class.getName()).replace(".", "/") + ".class";
	        extURL = extURL.replace(suffix, "");
	        if (extURL.startsWith("jar:") && extURL.endsWith(".jar!"))
	            extURL = extURL.substring(4, extURL.length() - 1);
	    }

	    try {
	        return new File(new URL(extURL).toURI());
	    } catch(URISyntaxException ex) {
	        return new File(new URL(extURL).getPath());
	    }
	}
}
