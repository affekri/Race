package net.derfla.race.utils;

import net.derfla.race.Race;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class raceTime {

    private final Race plugin;

    private static final HashMap<String, Long> time = new HashMap<>();

    public raceTime(HashMap<String, Long> raceTime, Race plugin) {
        this.plugin = plugin;
    }

    public void start(Player player) {
        time.put(player.getName(), System.currentTimeMillis());
        player.sendMessage(ChatColor.YELLOW + "Timer started!");

    }

    public void stop(Player player) {
        Long finalRaceTime = System.currentTimeMillis() - time.get(player.getName());
        // Change time format to seconds
        Long secsRaceTime = finalRaceTime / 1000;
        // Save time if it's better than earlier attempts.
        if (plugin.getConfig().get(player.getName() + ".time") == null){
            plugin.getConfig().set(player.getName() + ".time", secsRaceTime);
            plugin.saveConfig();
            player.sendMessage(ChatColor.GREEN + "You finished and your time was " + secsRaceTime + "!");
        } else {
            Long prevTime = plugin.getConfig().getLong(player.getName() + ".time");
            if (secsRaceTime < prevTime) {
                plugin.getConfig().set(player.getName() + ".time", secsRaceTime);
                plugin.saveConfig();
                player.sendMessage(ChatColor.GREEN + "You finished and your time was " + secsRaceTime + ". Which i better than your last attempt!");
            } else {
                player.sendMessage(ChatColor.YELLOW + "You finished and your time was " + secsRaceTime + ". However this was not your fastest attempt.");
            }
        }
    }
}
