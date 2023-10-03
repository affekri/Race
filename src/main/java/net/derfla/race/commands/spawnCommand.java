package net.derfla.race.commands;

import net.derfla.race.Race;
import net.derfla.race.utils.spawn;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class spawnCommand implements CommandExecutor {

    private final Race plugin;

    public spawnCommand(Race plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)){
            commandSender.sendMessage(ChatColor.RED + "This command is only available for players!");
            return true;
        }
        Player player = (Player) commandSender;
        if (!(player.isOp())){
            // Teleport player to spawn
            teleportSpawn(player);
            return true;
        } else {
            if (strings.length == 0) {
                // Teleport player to spawn
                teleportSpawn(player);
                return true;
            } else if (strings[0].equals("set")) {
                // Set spawn att players location
                setSpawn(player);
            }
        }

        return true;
    }

    public void teleportSpawn(Player player) {
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

    public void setSpawn(Player player) {
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
}
