package com.github.deroq1337.nbs.bukkit;

import com.github.deroq1337.nbs.api.NbsSongManager;
import com.github.deroq1337.nbs.bukkit.data.BukkitNbsSongManager;
import com.github.deroq1337.nbs.bukkit.data.listeners.PlayerJoinListener;
import com.github.deroq1337.nbs.bukkit.data.listeners.PlayerQuitListener;
import com.github.deroq1337.nbs.bukkit.data.user.BukkitNbsUserRegistry;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class BukkitNbsSongPlugin extends JavaPlugin {

    private NbsSongManager songManager;
    private BukkitNbsUserRegistry userRegistry;

    @Override
    public void onEnable() {
        this.songManager = new BukkitNbsSongManager();
        songManager.loadSongs();

        this.userRegistry = new BukkitNbsUserRegistry(this);

        new PlayerJoinListener(this);
        new PlayerQuitListener(this);
    }

    @Override
    public void onDisable() {
        songManager.clearSongs();
    }
}
