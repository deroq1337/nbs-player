package com.github.deroq1337.nbs.data.listeners;

import com.github.deroq1337.nbs.NbsSongPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinListener implements Listener {

    private final @NotNull NbsSongPlugin plugin;

    public PlayerJoinListener(@NotNull NbsSongPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getUserRegistry().addUser(event.getPlayer().getUniqueId());
    }
}
