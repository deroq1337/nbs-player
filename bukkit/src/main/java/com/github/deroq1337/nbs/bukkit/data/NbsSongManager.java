package com.github.deroq1337.nbs.bukkit.data;

import com.github.deroq1337.nbs.api.Song;
import com.github.deroq1337.nbs.api.SongManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class NbsSongManager implements SongManager {

    @Override
    public Optional<Song> getSongById(int id) {
        return Optional.empty();
    }

    @Override
    public @NotNull List<Song> getSongs() {
        return List.of();
    }
}
