package com.github.deroq1337.nbs.api;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface NbsSongManager {

    @NotNull CompletableFuture<Void> loadSongs();

    void clearSongs();

    Optional<NbsSong> getSongByName(@NotNull String name);

    @NotNull Collection<NbsSong> getSongs();
}
