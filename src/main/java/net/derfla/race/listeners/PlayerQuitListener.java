package net.derfla.race.listeners;

import net.derfla.race.Race;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final Race plugin;

    public PlayerQuitListener(Race plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // Resets the checkpoint
        plugin.getConfig().set(player.getName() + ".checkpoint", null);
        plugin.saveConfig();
    }
}
