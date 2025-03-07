package com.alicedev.rejuvenationTools;

import com.sun.net.httpserver.HttpServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public final class RejuvenationTools extends JavaPlugin {

    private static HttpServer server;
    private static RejuvenationTools instance;

    @Override
    public void onEnable() {
        getLogger().info("Initializing plugin...");

        try {
            server = HttpServer.create(new InetSocketAddress(7867), 0);
            server.setExecutor(Executors.newCachedThreadPool());
            server.start();
        } catch (IOException e) {
            disablePlugin("Could not open the port for the web server: " + e.getMessage());
        }

        getLogger().info("Plugin successfully initialized.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void disablePlugin(String message) {
        System.out.println(getPrefix()+message);
        Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin(getInstance().getName()));
    }

    public static String getPrefix() {
        return "["+getInstance().getName()+"] ";
    }

    public static RejuvenationTools getInstance() {
        return instance;
    }
}
