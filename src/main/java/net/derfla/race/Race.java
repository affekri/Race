package net.derfla.race;

import net.derfla.race.commands.checkpointCommand;
import net.derfla.race.commands.raceCommand;
import net.derfla.race.commands.spawnCommand;
import net.derfla.race.listeners.PlayerQuitListener;
import net.derfla.race.listeners.PlayerInteractListener;
import net.derfla.race.listeners.PlayerJoinListener;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Race extends JavaPlugin implements Listener, CommandExecutor {


    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Race plugin has been enabled!");

        // Register events in the listeners
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);

        // Set executors for commands
        getCommand("checkpoint").setExecutor(new checkpointCommand(this));
        getCommand("race").setExecutor(new raceCommand());
        getCommand("spawn").setExecutor(new spawnCommand(this));

        // Save config file
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Race plugin is disabled");
    }
}
