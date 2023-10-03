package net.derfla.race.commands;

import net.derfla.race.Race;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class raceCommand implements CommandExecutor {

    private final Race plugin;

    public raceCommand(Race plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        HashMap<String, Integer> times = new HashMap<>();

        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            if (plugin.getConfig().getString(player.getName() + ".time") != null) {
                // Creates a hashmap with all the players who have finished the race with their time.
                times.put(player.getName(), plugin.getConfig().getInt(player.getName() + ".time"));
            }
        }

        if (plugin.getConfig().getString("finished") == null) {
            commandSender.sendMessage(ChatColor.YELLOW + "These are the players who have finished the race with their time: " + times);
        } else {
            commandSender.sendMessage(ChatColor.YELLOW + plugin.getConfig().getString("finished") + times);
        }
        return true;
    }
}
