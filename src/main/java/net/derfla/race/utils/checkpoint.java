package net.derfla.race.utils;

import net.derfla.race.Race;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class checkpoint {

    private static Race plugin;

    public checkpoint(Race plugin) {
        checkpoint.plugin = plugin;


    }


    public static void teleport(Player player) {
        // Teleport player to spawn

        String name = player.getName();
        Location location = plugin.getConfig().getLocation(name + ".spawn");
        if (location == null ){
            String nocheckmessage = plugin.getConfig().getString("no-checkpoint");
            if (nocheckmessage != null){
                player.sendMessage(ChatColor.RED + nocheckmessage);
            } else {
                player.sendMessage(ChatColor.RED + "You have no checkpoint yet!");
            }

        }else{
            player.teleport(location);
            String teleportmessage = plugin.getConfig().getString("teleported-spawn");
            if (teleportmessage != null){
                player.sendMessage(ChatColor.GREEN + teleportmessage);
            } else {
                System.out.println("teleported-checkpoint = null!");
                player.sendMessage(ChatColor.GREEN + "Teleported to last checkpoint!");
            }
        }



    }

    public static void set (Player player) {
        // Set checkpoint at players location
        // Get location and name of the player
        Location location = player.getLocation();
        String name = player.getName();
        // Sets the location of the checkpoint in the config
        plugin.getConfig().set(name + ".checkpoint", location);
        // Saves the config
        plugin.saveConfig();
        String setmessage = plugin.getConfig().getString("set-checkpoint");
        if (setmessage != null){
            player.sendMessage(ChatColor.GREEN + setmessage);
        } else {
            System.out.println("set-checkpoint = null!");
            player.sendMessage(ChatColor.GREEN + "Checkpoint!");
        }

    }

}
