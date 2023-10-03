package net.derfla.race.commands;

import net.derfla.race.Race;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class checkpointCommand implements CommandExecutor {

    private final Race plugin;

    public checkpointCommand(Race plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)){
            commandSender.sendMessage(ChatColor.RED + "This command is only available for players!");
            return true;
        }
        Player player = (Player) commandSender;
        teleportCheckpoint(player);
        return true;
    }

    public void teleportCheckpoint(Player player) {
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
}
