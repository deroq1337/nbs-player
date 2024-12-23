package com.github.deroq1337.nbs.bukkit;

import com.github.deroq1337.nbs.api.NbsSongManager;
import com.github.deroq1337.nbs.bukkit.data.DefaultNbsSongManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class NoteBlockSongPlugin extends JavaPlugin {

    private NbsSongManager songManager;

    @Override
    public void onEnable() {
        this.songManager = new DefaultNbsSongManager();
        songManager.loadSongs();
    }
}
