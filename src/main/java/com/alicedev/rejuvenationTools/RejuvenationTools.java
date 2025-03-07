package com.alicedev.rejuvenationTools;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.plugin.java.JavaPlugin;

public final class RejuvenationTools extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Initializing plugin...");

        getLogger().info("Plugin successfully initialized.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
