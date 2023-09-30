package net.derfla.race.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class raceTime {

    private static final HashMap<String, Long> time = new HashMap<>();

    public raceTime(HashMap<String, Long> raceTime) {
    }

    public static void start(Player player) {
        time.put(player.getName(), System.currentTimeMillis());
        player.sendMessage(ChatColor.YELLOW + "Timer started!");

    }

    public void stop (Player player) {
        Long finalRaceTime = System.currentTimeMillis() - time.get(player.getName());
        // TODO change time format
        Long secsRaceTime = finalRaceTime / 1000;
        player.sendMessage(ChatColor.YELLOW + "Your time is " + secsRaceTime);
        // TODO Save time if it's better than earlier attempts.

    }
}
