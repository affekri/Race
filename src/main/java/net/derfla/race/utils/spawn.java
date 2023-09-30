package net.derfla.race.utils;

import net.derfla.race.Race;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public class spawn {

    private static Race plugin;

    public spawn(Race plugin) {
        this.plugin = plugin;
    }



    public static void teleport(Player player) {
        // Teleport player to spawn

        Location location = plugin.getConfig().getLocation("spawn");
        if (location == null ){
            player.sendMessage(ChatColor.RED + "Spawn has not been set yet!");
        }else{
            player.teleport(location);
            String teleportmessage = plugin.getConfig().getString("teleported-spawn");
            if (teleportmessage != null){
                player.sendMessage(ChatColor.GREEN + teleportmessage);
            } else {
                System.out.println("teleported-spawn = null!");
                player.sendMessage(ChatColor.GREEN + "Teleported to spawn!");
            }
        }



    }

    public static void set(Player player) {
        // Set spawn at players location
        // Get location of the player
        Location location = player.getLocation();
        // Sets the location to spawn in the config
        plugin.getConfig().set("spawn", location);
        // Saves the config
        plugin.saveConfig();
        String setmessage = plugin.getConfig().getString("set-spawn");
        if (setmessage != null){
            player.sendMessage(ChatColor.GREEN + setmessage);
        } else {
            System.out.println("set-spawn = null!");
            player.sendMessage(ChatColor.GREEN + "Set spawn at your location!");
        }

    }

    public static String test = plugin.getConfig().getString("set-spawn");
}
