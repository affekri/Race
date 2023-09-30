package net.derfla.race.commands;

import net.derfla.race.utils.checkpoint;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class checkpointCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)){
            commandSender.sendMessage(ChatColor.RED + "This command is only available for players!");
            return true;
        }
        Player player = (Player) commandSender;
        checkpoint.teleport(player);
        return true;
    }
}
