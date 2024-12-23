package com.github.deroq1337.nbs.api;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public interface SongLoader {

    @NotNull List<Song> loadSongs(@NotNull String path);

    @NotNull Song loadSong(@NotNull File file);
}
