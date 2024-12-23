package com.github.deroq1337.nbs.api;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface SongManager {

    Optional<Song> getSongById(int id);

    @NotNull List<Song> getSongs();
}
