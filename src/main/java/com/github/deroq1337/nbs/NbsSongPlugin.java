package com.github.deroq1337.nbs;

import com.github.deroq1337.nbs.data.NbsSongManager;
import com.github.deroq1337.nbs.data.command.NbsCommand;
import com.github.deroq1337.nbs.data.listeners.PlayerJoinListener;
import com.github.deroq1337.nbs.data.listeners.PlayerQuitListener;
import com.github.deroq1337.nbs.data.user.NbsUserRegistry;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class NbsSongPlugin extends JavaPlugin {

    private NbsSongManager songManager;
    private NbsUserRegistry userRegistry;

    @Override
    public void onEnable() {
        this.songManager = new NbsSongManager();
        songManager.loadSongs();

        this.userRegistry = new NbsUserRegistry(this);

        new PlayerJoinListener(this);
        new PlayerQuitListener(this);

        new NbsCommand(this);
    }

    @Override
    public void onDisable() {
        songManager.clearSongs();
    }
}
