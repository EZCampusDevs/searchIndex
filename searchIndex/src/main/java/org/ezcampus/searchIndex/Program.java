package org.ezcampus.searchIndex;

import jakarta.ws.rs.ApplicationPath;
import org.ezcampus.searchIndex.System.GlobalSettings;
import org.ezcampus.searchIndex.System.ResourceLoader;
import org.tinylog.Logger;

@ApplicationPath("/")
public class Program extends jakarta.ws.rs.core.Application
{
    // True Main Method here!!!
    public Program()
    {
        GlobalSettings.IS_DEBUG = true;

        ResourceLoader.loadTinyLogConfig();

        Logger.info("{} starting...", GlobalSettings.BRAND_LONG);
        Logger.info("Running as debug: {}", GlobalSettings.IS_DEBUG);


    }
}