package net.derfla.race.listeners;

import net.derfla.race.Race;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;

public class PlayerInteractListener implements Listener {

    private final Race plugin;
    private static final HashMap<String, Long> time = new HashMap<>();

    public PlayerInteractListener(Race plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if(event.getAction().equals(Action.PHYSICAL)){
            if (event.getClickedBlock().getType().equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)) {
                // start timer
                startTime(player);
            } else if (event.getClickedBlock().getType().equals(Material.HEAVY_WEIGHTED_PRESSURE_PLATE)) {
                // end timer
                stopTime(player);
                resetCheckpoint(player);
            } else if (event.getClickedBlock().getType().equals(Material.STONE_PRESSURE_PLATE)) {
                // teleport to last checkpoint
                teleportCheckpoint(player);
            } else if (event.getClickedBlock().getType().equals(Material.OAK_PRESSURE_PLATE)) {
                // set checkpoint
                setCheckpoint(player);
            }
        }
    }

    public void startTime(Player player) {
        time.put(player.getName(), System.currentTimeMillis());
        player.sendMessage(ChatColor.YELLOW + "Timer started!");
    }

    public void stopTime(Player player) {
        if (time.get(player.getName()) == null){
            if (plugin.getConfig().getString("race-not-started") == null) {
                player.sendMessage(ChatColor.RED + "You have not started the race!");
            }else {
                player.sendMessage(ChatColor.RED + plugin.getConfig().getString("race-not-started"));
            }

            return;
        }
        Long finalRaceTime = System.currentTimeMillis() - time.get(player.getName());
        // Change time format to seconds
        Long secsRaceTime = finalRaceTime / 1000;
        // Save time if it's better than earlier attempts.
        time.put(player.getName(), null);
        if (plugin.getConfig().get(player.getName() + ".time") == null){
            plugin.getConfig().set(player.getName() + ".time", secsRaceTime);
            plugin.saveConfig();
            if (plugin.getConfig().getString("player-finished-first") == null) {
                player.sendMessage(ChatColor.GREEN + "You finished and your time was " + secsRaceTime + "!");
            }else {
                player.sendMessage(ChatColor.GREEN + plugin.getConfig().getString("player-finished-first") + secsRaceTime + "!");
            }
        } else {
            Long prevTime = plugin.getConfig().getLong(player.getName() + ".time");
            if (secsRaceTime < prevTime) {
                plugin.getConfig().set(player.getName() + ".time", secsRaceTime);
                plugin.saveConfig();
                if (plugin.getConfig().getString("player-finished-better") == null) {
                    player.sendMessage(ChatColor.GREEN + "You finished and your time was " + secsRaceTime + ". Which i better than your last attempt!");
                }else {
                    player.sendMessage(ChatColor.GREEN + plugin.getConfig().getString("player-finished-first") + secsRaceTime + plugin.getConfig().getString("player-finished-better"));
                }
            } else {
                if (plugin.getConfig().getString("player-finished-worse") == null) {
                    player.sendMessage(ChatColor.GREEN + "You finished and your time was " + secsRaceTime + ". However this was not your fastest attempt.");
                }else {
                    player.sendMessage(ChatColor.GREEN + plugin.getConfig().getString("player-finished-first") + secsRaceTime + plugin.getConfig().getString("player-finished-worse"));
                }
            }
        }
    }

    public void teleportCheckpoint(Player player) {
        // Teleport player to spawn
        String name = player.getName();
        Location location = plugin.getConfig().getLocation(name + ".checkpoint");
        if (location == null ){
            String nocheckmessage = plugin.getConfig().getString("no-checkpoint");
            if (nocheckmessage != null){
                player.sendMessage(ChatColor.RED + nocheckmessage);
            } else {
                player.sendMessage(ChatColor.RED + "You have no checkpoint yet!");
            }
        }else{
            player.teleport(location);
            String teleportmessage = plugin.getConfig().getString("teleported-checkpoint");
            if (teleportmessage != null){
                player.sendMessage(ChatColor.GREEN + teleportmessage);
            } else {
                System.out.println("teleported-checkpoint = null!");
                player.sendMessage(ChatColor.GREEN + "Teleported to last checkpoint!");
            }
        }
    }

    public void setCheckpoint (Player player) {
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

    public void resetCheckpoint (Player player) {
        plugin.getConfig().set(player.getName() + ".checkpoint", null);
        plugin.saveConfig();
    }
}
