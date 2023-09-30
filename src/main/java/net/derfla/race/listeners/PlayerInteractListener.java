package net.derfla.race.listeners;

import net.derfla.race.utils.checkpoint;
import net.derfla.race.utils.raceTime;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {




    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if(event.getAction().equals(Action.PHYSICAL)){
            if (event.getClickedBlock().equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)){
                // start timer
                raceTime.start(player);
                player.sendMessage("start");
            } else if (event.getClickedBlock().equals(Material.HEAVY_WEIGHTED_PRESSURE_PLATE)) {
                // end timer

            } else if (event.getClickedBlock().equals(Material.STONE_PRESSURE_PLATE)) {
                // teleport to last checkpoint
                checkpoint.teleport(player);
            } else if (event.getClickedBlock().equals(Material.OAK_PRESSURE_PLATE)) {
                // set checkpoint
                checkpoint.set(player);
            }
        }

    }
}
