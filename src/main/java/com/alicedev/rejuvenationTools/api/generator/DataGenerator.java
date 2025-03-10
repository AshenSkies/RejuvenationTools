package com.alicedev.rejuvenationTools.api.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Stream;

public class DataGenerator {

    Plugin plugin = Bukkit.getPluginManager().getPlugin("RejuvenationTools");

    public void generateData(String pluginName,String folderToDrill,String outputName) {
        File datafolder = Bukkit.getPluginManager().getPlugin(pluginName).getDataFolder();

        ArrayList<String> dataCollection = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get(datafolder.getAbsolutePath() + folderToDrill))) {

            paths
                    .filter(Files::isRegularFile)  // ensure it is a file
                    .filter(e -> e.toString().contains(".yml"))
                    .forEach(e -> {
                        boolean record;
                        Set<String> keys = YamlConfiguration.loadConfiguration(e.toFile()).getKeys(false);
                        for (String key : keys) {
                            record = YamlConfiguration.loadConfiguration(e.toFile()).getConfigurationSection(key).getBoolean("Docs");
                            if (record) {
                                ConfigurationSection section = YamlConfiguration.loadConfiguration(e.toFile()).getConfigurationSection(key);
                                YamlConfiguration tempConfig = new YamlConfiguration();
                                tempConfig.set(key,section);
                                try {
                                    String finaljson = convertYamltoJson(tempConfig.saveToString());
                                    dataCollection.add(finaljson);
                                } catch (JsonProcessingException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        }
                    }); // prints the file path

        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        createDataFile(outputName + ".json");
        writeDataFile(outputName + ".json",dataCollection.toString());
    }

    @Deprecated
    public void old_generateData() {
        File datafolder = Bukkit.getPluginManager().getPlugin("MythicMobs").getDataFolder();

        ArrayList<String> dataCollection = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get(datafolder.getAbsolutePath() + "/Mobs"))) {

            paths
                    .filter(Files::isRegularFile)  // ensure it is a file
                    .forEach(e -> {
                        boolean record;
                        Set<String> keys = YamlConfiguration.loadConfiguration(e.toFile()).getKeys(false);
                        for (String key : keys) {
                            record = YamlConfiguration.loadConfiguration(e.toFile()).getConfigurationSection(key).getBoolean("Docs");
                            if (record) {
                                ConfigurationSection section = YamlConfiguration.loadConfiguration(e.toFile()).getConfigurationSection(key);
                                YamlConfiguration tempConfig = new YamlConfiguration();
                                tempConfig.set(key,section);
                                try {
                                    String finaljson = convertYamltoJson(tempConfig.saveToString());
                                    dataCollection.add(finaljson);
                                } catch (JsonProcessingException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        }
                    }); // prints the file path

        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        createDataFile("mobs.json");
        writeDataFile("mobs.json",dataCollection.toString());

        plugin.getLogger().info(dataCollection.toString());
    }

    public String convertYamltoJson(String yaml) throws JsonProcessingException {
        ObjectMapper yamlreader = new ObjectMapper(new YAMLFactory());
        Object obj = yamlreader.readValue(yaml, Object.class);

        ObjectMapper jsonwriter = new ObjectMapper();
        return jsonwriter.writeValueAsString(obj);
    }

    // Ensures the file in the given filename path exists
    public void createDataFile(String filename){
        try {
            File myObj = new File(plugin.getDataFolder().getAbsolutePath() + "/output/" + filename);
            if (myObj.createNewFile()) {
                plugin.getLogger().warning("File created: " + myObj.getName());
            } else {
                plugin.getLogger().warning("Failed at generating json file: File already exists!");
            }
        } catch (IOException e) {
            plugin.getLogger().warning("An error occurred.");
            e.printStackTrace();
        }
    }

    // Writes content to the file in the given filename path
    public void writeDataFile(String filename, String content){
        try {
            FileWriter myWriter = new FileWriter(plugin.getDataFolder().getAbsolutePath() + "/output/" + filename);
            myWriter.write(content);
            myWriter.close();
            plugin.getLogger().warning("Successfully wrote to the file.");
        } catch (IOException e) {
            plugin.getLogger().warning("An error occurred.");
            e.printStackTrace();
        }
    }
}
