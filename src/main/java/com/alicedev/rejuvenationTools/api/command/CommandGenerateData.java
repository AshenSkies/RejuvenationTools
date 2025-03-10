package com.alicedev.rejuvenationTools.api.command;

import com.alicedev.rejuvenationTools.RejuvenationTools;
import com.alicedev.rejuvenationTools.api.generator.DataGenerator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CommandGenerateData implements CommandExecutor, TabCompleter {

    RejuvenationTools plugin;

    public CommandGenerateData(RejuvenationTools plugin){
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> list = new ArrayList<>();


        if(!(commandSender instanceof Player)) return null;
        if (!(command.getName().equalsIgnoreCase("generate") || command.getName().equalsIgnoreCase("gen"))) return null;
        Set<String> data_entries = plugin.getConfig().getConfigurationSection("generate").getKeys(false);
        list.add("all");

        if (strings.length == 1) {

            for (String entry : data_entries) {
                list.add(entry);
            }
            Collections.sort(list);
            return list;

        } else if (strings.length == 2) {

            for (String entry : data_entries) {
                if (!entry.toLowerCase().startsWith(strings[0].toLowerCase())) list.add(entry);
            }
            Collections.sort(list);
            return list;
        }

        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cNo data ID given. Usage: /generate <data-id>");
            return false;
        }

        if (!(plugin.getConfig().getConfigurationSection("generate").getKeys(false).contains(args[0]) || args[0].equalsIgnoreCase("all"))) {
            sender.sendMessage("§cValid data ID not found. (Check config.yml?)");
            return false;
        }

        DataGenerator gen = new DataGenerator();

        if (args[0].equalsIgnoreCase("all")) {
            for (String data_id : plugin.getConfig().getConfigurationSection("generate").getKeys(false)) {
                ConfigurationSection conf = plugin.getConfig().getConfigurationSection("generate").getConfigurationSection(data_id);
                gen.generateData(conf.getString("plugin"),conf.getString("folder"),data_id);
            }
            sender.sendMessage("§x§1§2§f§f§a§cSuccessfully generated data file of ID '" + args[0] + "'!");
        } else {
            ConfigurationSection conf = plugin.getConfig().getConfigurationSection("generate").getConfigurationSection(args[0]);
            gen.generateData(conf.getString("plugin"),conf.getString("folder"),args[0]);
            sender.sendMessage("§x§1§2§f§f§a§cSuccessfully generated data file of ID '" + args[0] + "'!");
        }

        return true;
    }
}
