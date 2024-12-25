package com.github.deroq1337.nbs.bukkit.data.listeners;

import com.github.deroq1337.nbs.api.NbsUser;
import com.github.deroq1337.nbs.bukkit.BukkitNbsSongPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerQuitListener implements Listener {

    private final @NotNull BukkitNbsSongPlugin plugin;

    public PlayerQuitListener(@NotNull BukkitNbsSongPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getUserRegistry().getUser(event.getPlayer().getUniqueId()).ifPresent(NbsUser::leaveSongSession);
        plugin.getUserRegistry().removeUser(event.getPlayer().getUniqueId());
    }
}
