package com.github.deroq1337.nbs.api;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface NbsSongSession {

    void play();

    void stop();

    void pause();

    void resume();

    boolean isPlaying();

    boolean hasStarted();

    int getCurrentTick();

    void setCurrentTick(int tick);

    @NotNull NbsSong getSong();

    @NotNull NbsUser getOwner();

    void addListeningUser(@NotNull NbsUser user);

    void removeListeningUser(@NotNull NbsUser user);

    @NotNull List<NbsUser> getListeningUsers();
}
