package net.derfla.race.commands;

import net.derfla.race.utils.spawn;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class spawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)){
            commandSender.sendMessage(ChatColor.RED + "This command is only available for players!");
            return true;
        }
        Player player = (Player) commandSender;
        if (!(player.isOp())){
            // Teleport player to spawn
            spawn.teleport(player);
            return true;
        } else {
            if (strings.length == 0) {
                // Teleport player to spawn
                spawn.teleport(player);
                return true;
            } else if (strings[0].equals("set")) {
                // Set spawn att players location
                spawn.set(player);
            }
        }

        return true;
    }
}
