package com.github.deroq1337.nbs.api;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

public interface NbsSongSession {

    void play();

    void stop();

    void disband();

    void pause();

    void resume();

    void join(@NotNull NbsUser user);

    void leave(@NotNull NbsUser user);

    void setCurrentSong(@NotNull NbsSong song);

    void setCurrentTick(int tick);

    @NotNull NbsUser getOwner();

    Optional<NbsSong> getCurrentSong();

    @NotNull Set<NbsUser> getListeningUsers();

    boolean isOwner(@NotNull NbsUser user);

    boolean isCurrentSong(@NotNull NbsSong song);

    boolean hasStarted();

    boolean isPlaying();

    int getCurrentTick();
}
