package com.github.deroq1337.nbs.bukkit.data;

import com.github.deroq1337.nbs.api.NbsSong;
import com.github.deroq1337.nbs.api.NbsSongLoader;
import com.github.deroq1337.nbs.api.NbsSongManager;
import com.github.deroq1337.nbs.bukkit.data.loader.DefaultNbsSongLoader;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class DefaultNbsSongManager implements NbsSongManager {

    private static final String SONGS_PATH = "plugins/nbs/songs/";

    private final @NotNull NbsSongLoader songLoader;
    private final @NotNull Map<String, NbsSong> songsMap = new HashMap<>();

    public DefaultNbsSongManager() {
        this.songLoader = new DefaultNbsSongLoader();
    }

    @Override
    public @NotNull CompletableFuture<Void> loadSongs() {
        return songLoader.loadSongs(SONGS_PATH).thenAccept(songs -> {
            synchronized (songsMap) {
                songsMap.clear();
                songs.forEach(song -> songsMap.put(song.name(), song));
            }
        });
    }

    @Override
    public void clearSongs() {
        synchronized (songsMap) {
            songsMap.clear();
        }
    }

    @Override
    public Optional<NbsSong> getSongByName(@NotNull String name) {
        return Optional.ofNullable(songsMap.get(name));
    }

    @Override
    public @NotNull Collection<NbsSong> getSongs() {
        return songsMap.values();
    }
}
