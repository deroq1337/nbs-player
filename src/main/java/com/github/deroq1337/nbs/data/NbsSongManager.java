package com.github.deroq1337.nbs.data;

import com.github.deroq1337.nbs.data.loader.NbsSongLoader;
import com.github.deroq1337.nbs.data.models.NbsSong;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class NbsSongManager {

    private static final String SONGS_PATH = "plugins/nbs/songs/";

    private final @NotNull NbsSongLoader songLoader;
    private final @NotNull Map<String, NbsSong> songsMap = new HashMap<>();

    public NbsSongManager() {
        this.songLoader = new com.github.deroq1337.nbs.data.loader.NbsSongLoader();
    }

    public @NotNull CompletableFuture<Void> loadSongs() {
        return songLoader.loadSongs(SONGS_PATH).thenAccept(songs -> {
            synchronized (songsMap) {
                songsMap.clear();
                songs.forEach(song -> songsMap.put(song.name().toLowerCase(), song));
            }
        });
    }

    public void clearSongs() {
        synchronized (songsMap) {
            songsMap.clear();
        }
    }

    public Optional<NbsSong> getSongByName(@NotNull String name) {
        return Optional.ofNullable(songsMap.get(name.toLowerCase()));
    }

    public @NotNull Collection<NbsSong> getSongs() {
        return songsMap.values();
    }
}
