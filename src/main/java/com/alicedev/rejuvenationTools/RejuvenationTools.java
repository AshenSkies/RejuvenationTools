package com.alicedev.rejuvenationTools;

import com.alicedev.rejuvenationTools.api.jettyserver.WebServerManager;
import com.sun.net.httpserver.HttpServer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class RejuvenationTools extends JavaPlugin {

    private WebServerManager serverManager;
    private static final Logger logger = LoggerFactory.getLogger("");

    @Override
    public void onEnable() {
        getLogger().info("Initializing plugin...");

        // Plugin startup logic
        WebServerManager serverManager = new WebServerManager(this);
        loadConfig();
        copyIndexHtml();
        registerCommands();
        serverManager.jettyStart();
        logger.info("Plugin enabled!");

        getLogger().info("Plugin successfully initialized.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (serverManager != null) {
            serverManager.jettyStop();
        }
        logger.info("Plugin disabled!");
    }

    private void loadConfig() {
        // Load config
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    private class SimpleWebServerCommands implements CommandExecutor, TabCompleter {

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You must be a player to use this command!");
                return true;
            }

            Player player = (Player) sender;

            // Check the main permission
            if (!player.hasPermission("sws.use")) {
                player.sendMessage("You do not have permission to use this command!");
                return true;
            }

            if (args.length == 0) {
                player.sendMessage("/sws <start|stop|restart|link>");
                return true;
            }

            // Check the first argument (suffix) and call the corresponding method
            switch (args[0].toLowerCase()) {
                case "stop":
                    if (player.hasPermission("sws.admin")){
                        serverManager.jettyStop();

                    } else {
                        player.sendMessage("You do not have permission to use this command!");
                    }
                    break;
                case "start":
                    if (player.hasPermission("sws.admin")){
                        serverManager.jettyStart();

                    } else {
                        player.sendMessage("You do not have permission to use this command!");
                    }
                    break;
                case "restart":
                    if (player.hasPermission("sws.admin")){
                        serverManager.jettyRestart();

                    } else {
                        player.sendMessage("You do not have permission to use this command!");
                    }
                    break;
                case "link":
                    if (player.hasPermission("sws.link")) {
                        String serverIP = Bukkit.getServer().getIp();
                        int webServerPort = getConfig().getInt("web-server-port");

                        String url = "http://" + serverIP + ":" + webServerPort;

                        String clickMessage = "Click here to open the web server.";
                    } else {
                        player.sendMessage("You do not have permission to use this command!");
                    }
                    break;
                default:
                    player.sendMessage("Invalid argument. Use /sws <start|stop|restart|link>");
                    break;
            }

            return true;
        }
        @Override
        public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
            List<String> completions = new ArrayList<>();

            if (args.length == 1) {
                // Provide tab completion for the first argument
                String partialCommand = args[0].toLowerCase();

                // Add your command names here
                List<String> commandNames = List.of("start", "stop", "restart", "link");

                for (String command : commandNames) {
                    if (command.startsWith(partialCommand)) {
                        completions.add(command);
                    }
                }
            }

            // You can add more tab completion logic for additional arguments if needed

            return completions;
        }
    }


    private void registerCommands() {
        this.getCommand("rts").setExecutor(new SimpleWebServerCommands());
    }

    private void copyIndexHtml() {
        Path pluginFolder = getDataFolder().toPath();
        Path indexPath = pluginFolder.resolve("web/index.html");

        if (!Files.exists(indexPath)) {
            // The file does not exist, so copy it from resources
            getLogger().info("Web files do not exist. Attempting to create from base...");
            try {
                Files.createDirectories(indexPath.getParent());
                Files.copy(getResource("web/index.html"), indexPath, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(getResource("web/universal.css"), indexPath, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(getResource("web/main.js"), indexPath, StandardCopyOption.REPLACE_EXISTING);

                getLogger().info("Web files successfully created.");

            } catch (IOException e) {
                logger.error("Error copying index.html: " + e.getMessage());
            }
        }
    }
}
