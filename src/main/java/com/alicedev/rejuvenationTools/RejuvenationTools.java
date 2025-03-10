package com.alicedev.rejuvenationTools;

import com.alicedev.rejuvenationTools.api.command.CommandGenerateData;
import com.alicedev.rejuvenationTools.api.generator.DataGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class RejuvenationTools extends JavaPlugin {


    @Override
    public void onEnable() {
        getLogger().info("Initializing plugin...");

        createDataFolders();
        runGenerations();
        registerCommands();

        getLogger().info("Plugin successfully initialized.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin disabled.");
    }

    public void runGenerations() {
        DataGenerator gen = new DataGenerator();

        gen.generateData("MythicMobs","/Mobs", "mobs");
        gen.generateData("MythicMobs","/Items", "mythicitems");
    }

    public void createDataFolders(){

        if(!this.getDataFolder().exists()) {
            try {
                this.getDataFolder().mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File temp = new File(getDataFolder().getAbsolutePath() + "/output");
        if (!temp.exists()) {
            try {
                temp.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void registerCommands() {
        this.getCommand("generate").setExecutor(new CommandGenerateData());
    }
}
