package net.derfla.race.listeners;

import net.derfla.race.Race;
import org.bukkit.*;
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
        player.playNote(player.getLocation(), Instrument.PIANO, new Note(8));
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
        player.playSound(player.getLocation(), "entity.firework_rocket.large_blast", 1.0f, 1.0f);
        Long finalRaceTime = System.currentTimeMillis() - time.get(player.getName());
        // Change time format to seconds
        float secsRaceTime = finalRaceTime / 1000f;
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
            String noCheckMessage = plugin.getConfig().getString("no-checkpoint");
            if (noCheckMessage != null){
                player.sendMessage(ChatColor.RED + noCheckMessage);
            } else {
                player.sendMessage(ChatColor.RED + "You have no checkpoint yet!");
            }
        }else{
            player.teleport(location);
            player.playSound(player.getLocation(), "entity.player.small_fall", 1.0f, 1.0f);
            String teleportMessage = plugin.getConfig().getString("teleported-checkpoint");
            if (teleportMessage != null){
                player.sendMessage(ChatColor.GREEN + teleportMessage);
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
        player.playSound(player.getLocation(), "entity.arrow.hit_player", 1.0f, 1.0f);
        String setMessage = plugin.getConfig().getString("set-checkpoint");
        if (setMessage != null){
            player.sendMessage(ChatColor.GREEN + setMessage);
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
