package com.github.deroq1337.nbs.bukkit.data.listeners;

import com.github.deroq1337.nbs.bukkit.BukkitNbsSongPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinListener implements Listener {

    private final @NotNull BukkitNbsSongPlugin plugin;

    public PlayerJoinListener(@NotNull BukkitNbsSongPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getUserRegistry().addUser(event.getPlayer().getUniqueId());
    }
}
