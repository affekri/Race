package net.derfla.race;

import net.derfla.race.commands.checkpointCommand;
import net.derfla.race.commands.raceCommand;
import net.derfla.race.commands.spawnCommand;
import net.derfla.race.utils.checkpoint;
import net.derfla.race.utils.raceTime;
import net.derfla.race.listeners.PlayerInteractListener;
import net.derfla.race.listeners.PlayerJoinListener;
import net.derfla.race.utils.spawn;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Race extends JavaPlugin implements Listener, CommandExecutor {


    @Override
    public void onEnable() {

        // Plugin startup logic
        getLogger().info("Race plugin has been enabled!");

        // TEMP
        getServer().getPluginManager().registerEvents(this, this);



        // Adds listeners for events
        /*
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);


        // Set executors for commands
        getCommand("checkpoint").setExecutor(new checkpointCommand());
        getCommand("race").setExecutor(new raceCommand());
        getCommand("spawn").setExecutor(new spawnCommand());

         */

        // Save config file
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        // DEBUG
        //getLogger().info(getConfig().getString("set-spawn"));
        //getLogger().info(spawn.test);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Race plugin is disabled");
    }

    // TEMP
    //  |
    //  V

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        getLogger().info("interactEvent");

        if(event.getAction().equals(Action.PHYSICAL)){
            getLogger().info("physical");
            if (event.getClickedBlock().getType().equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)){
                // start timer
                time.put(player.getName(), System.currentTimeMillis());
                player.sendMessage(ChatColor.YELLOW + "Timer started!");

            } else if (event.getClickedBlock().getType().equals(Material.HEAVY_WEIGHTED_PRESSURE_PLATE)) {
                // end timer
                Long finalRaceTime = System.currentTimeMillis() - time.get(player.getName());
                // Changes to seconds
                Long secsRaceTime = finalRaceTime / 1000;

                // Save time if it's better than earlier attempts.
                if (getConfig().get(player.getName() + ".time") == null){
                    getConfig().set(player.getName() + ".time", secsRaceTime);
                    saveConfig();
                    player.sendMessage(ChatColor.GREEN + "You finished and your time was " + secsRaceTime + "!");
                } else {
                    Long prevTime = getConfig().getLong(player.getName() + ".time");
                    if (secsRaceTime < prevTime) {
                        getConfig().set(player.getName() + ".time", secsRaceTime);
                        saveConfig();
                        player.sendMessage(ChatColor.GREEN + "You finished and your time was " + secsRaceTime + ". Which i better than your last attempt!");
                    } else {
                        player.sendMessage(ChatColor.YELLOW + "You finished and your time was " + secsRaceTime + ". However this was not your fastest attempt.");
                    }
                }





            } else if (event.getClickedBlock().getType().equals(Material.STONE_PRESSURE_PLATE)) {
                // teleport to last checkpoint
                String name = player.getName();
                Location location = getConfig().getLocation(name + ".checkpoint");
                if (location == null ){
                    String nocheckmessage = getConfig().getString("no-checkpoint");
                    if (nocheckmessage != null){
                        player.sendMessage(ChatColor.RED + nocheckmessage);
                    } else {
                        player.sendMessage(ChatColor.RED + "You have no checkpoint yet!");
                    }

                }else{
                    player.teleport(location);
                    String teleportmessage = getConfig().getString("teleported-spawn");
                    if (teleportmessage != null){
                        player.sendMessage(ChatColor.GREEN + teleportmessage);
                    } else {
                        System.out.println("teleported-checkpoint = null!");
                        player.sendMessage(ChatColor.GREEN + "Teleported to last checkpoint!");
                    }
                };
            } else if (event.getClickedBlock().getType().equals(Material.OAK_PRESSURE_PLATE)) {
                // set checkpoint
                getLogger().info("setCheckpoint");
                // Set checkpoint at players location
                // Get location and name of the player
                Location location = player.getLocation();
                String name = player.getName();
                // Sets the location of the checkpoint in the config
                getConfig().set(name + ".checkpoint", location);
                // Saves the config
                saveConfig();
                String setmessage = getConfig().getString("set-checkpoint");
                if (setmessage != null){
                    player.sendMessage(ChatColor.GREEN + setmessage);
                } else {
                    System.out.println("set-checkpoint = null!");
                    player.sendMessage(ChatColor.GREEN + "Checkpoint!");
                }
            }
        }

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Location location = getConfig().getLocation("spawn");
        if (location == null ){
            player.sendMessage(ChatColor.RED + "Spawn has not been set yet!");
        }else{
            player.teleport(location);
            String teleportmessage = getConfig().getString("teleported-spawn");
            if (teleportmessage != null){
                player.sendMessage(ChatColor.GREEN + teleportmessage);
            } else {
                System.out.println("teleported-spawn = null!");
                player.sendMessage(ChatColor.GREEN + "Teleported to spawn!");
            }
        }

    }

    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // Resets the checkpoint
        getConfig().set(player.getName() + ".checkpoint", null);
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)){
            commandSender.sendMessage(ChatColor.RED + "This command is only available for players!");
            return true;
        }
        Player player = (Player) commandSender;
        if (command.getName().equalsIgnoreCase("spawn")){

            if (!(player.isOp())){
                // Teleport player to spawn
                Location location = getConfig().getLocation("spawn");
                if (location == null ){
                    player.sendMessage(ChatColor.RED + "Spawn has not been set yet!");
                }else{
                    player.teleport(location);
                    String teleportmessage = getConfig().getString("teleported-spawn");
                    if (teleportmessage != null){
                        player.sendMessage(ChatColor.GREEN + teleportmessage);
                    } else {
                        System.out.println("teleported-spawn = null!");
                        player.sendMessage(ChatColor.GREEN + "Teleported to spawn!");
                    }
                }
                return true;
            } else {
                if (strings.length == 0) {
                    // Teleport player to spawn
                    Location location = getConfig().getLocation("spawn");
                    if (location == null ){
                        player.sendMessage(ChatColor.RED + "Spawn has not been set yet!");
                    }else{
                        player.teleport(location);
                        String teleportmessage = getConfig().getString("teleported-spawn");
                        if (teleportmessage != null){
                            player.sendMessage(ChatColor.GREEN + teleportmessage);
                        } else {
                            System.out.println("teleported-spawn = null!");
                            player.sendMessage(ChatColor.GREEN + "Teleported to spawn!");
                        }
                    }
                    return true;
                } else if (strings[0].equals("set")) {
                    // Set spawn att players location
                    // Set spawn at players location
                    // Get location of the player
                    Location location = player.getLocation();
                    // Sets the location to spawn in the config
                    getConfig().set("spawn", location);
                    // Saves the config
                    saveConfig();
                    String setmessage = getConfig().getString("set-spawn");
                    if (setmessage != null){
                        player.sendMessage(ChatColor.GREEN + setmessage);
                    } else {
                        System.out.println("set-spawn = null!");
                        player.sendMessage(ChatColor.GREEN + "Set spawn at your location!");
                    }
                }
            }
        } else if (command.getName().equalsIgnoreCase("checkpoint")) {
            String name = player.getName();
            Location location = getConfig().getLocation(name + ".spawn");
            if (location == null ){
                String nocheckmessage = getConfig().getString("no-checkpoint");
                if (nocheckmessage != null){
                    player.sendMessage(ChatColor.RED + nocheckmessage);
                } else {
                    player.sendMessage(ChatColor.RED + "You have no checkpoint yet!");
                }

            }else{
                player.teleport(location);
                String teleportmessage = getConfig().getString("teleported-spawn");
                if (teleportmessage != null){
                    player.sendMessage(ChatColor.GREEN + teleportmessage);
                } else {
                    System.out.println("teleported-checkpoint = null!");
                    player.sendMessage(ChatColor.GREEN + "Teleported to last checkpoint!");
                }
            }
            return true;
        }

        return true;
    }





    private static final HashMap<String, Long> time = new HashMap<>();


}
