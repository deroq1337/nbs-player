package com.github.deroq1337.nbs.api;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface NbsUser {

    @NotNull UUID getUuid();

    void startSongSession(@NotNull NbsSongSession songSession);

    void startSongSession(@NotNull NbsSong song);

    void stopSongSession();

    Optional<NbsSongSession> getSongSession();
}
