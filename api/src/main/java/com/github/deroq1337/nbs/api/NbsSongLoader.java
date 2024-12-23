package com.github.deroq1337.nbs.api;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface NbsSongLoader {

    @NotNull CompletableFuture<List<NbsSong>> loadSongs(@NotNull String path);

    @NotNull CompletableFuture<Optional<NbsSong>> loadSong(@NotNull File file);
}
