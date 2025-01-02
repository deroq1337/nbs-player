package com.github.deroq1337.nbs.data.listeners;

import com.github.deroq1337.nbs.NbsSongPlugin;
import com.github.deroq1337.nbs.data.user.NbsUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerQuitListener implements Listener {

    private final @NotNull NbsSongPlugin plugin;

    public PlayerQuitListener(@NotNull NbsSongPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getUserRegistry().getUser(event.getPlayer().getUniqueId()).ifPresent(NbsUser::leaveSongSession);
        plugin.getUserRegistry().removeUser(event.getPlayer().getUniqueId());
    }
}
