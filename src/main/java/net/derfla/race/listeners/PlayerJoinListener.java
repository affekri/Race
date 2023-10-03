package net.derfla.race.listeners;

import net.derfla.race.Race;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final Race plugin;

    public PlayerJoinListener(Race plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        teleportSpawn(player);
    }

    public void teleportSpawn(Player player) {
        // Teleport player to spawn

        Location location = plugin.getConfig().getLocation("spawn");
        if (location == null ){
            player.sendMessage(ChatColor.RED + "Spawn has not been set yet!");
        }else{
            player.teleport(location);
        }
    }
}
