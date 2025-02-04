package com.github.deroq1337.nbs.data;

import com.github.deroq1337.nbs.data.loader.NbsSongLoader;
import com.github.deroq1337.nbs.data.models.NbsSong;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class NbsSongManager {

    private final @NotNull NbsSongLoader songLoader = new NbsSongLoader(new File("plugins/nbs/songs/"));
    private final @NotNull Map<String, NbsSong> songsMap = new ConcurrentHashMap<>();

    public @NotNull CompletableFuture<Void> loadSongs() {
        return songLoader.loadSongs().thenAccept(songs -> {
            synchronized (songsMap) {
                songsMap.clear();;
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
