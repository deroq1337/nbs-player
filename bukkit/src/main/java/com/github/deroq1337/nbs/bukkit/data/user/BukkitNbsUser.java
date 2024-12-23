package com.github.deroq1337.nbs.bukkit.data.user;

import com.github.deroq1337.nbs.api.NbsSong;
import com.github.deroq1337.nbs.api.NbsSongSession;
import com.github.deroq1337.nbs.api.NbsUser;
import com.github.deroq1337.nbs.bukkit.BukkitNbsSongPlugin;
import com.github.deroq1337.nbs.bukkit.data.models.BukkitNbsSongSession;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class BukkitNbsUser implements NbsUser {

    private final @NotNull BukkitNbsSongPlugin plugin;
    private final @NotNull UUID uuid;
    private Optional<NbsSongSession> songSession = Optional.empty();

    public static @NotNull BukkitNbsUser create(@NotNull BukkitNbsSongPlugin plugin, @NotNull UUID uuid) {
        return new BukkitNbsUser(plugin, uuid);
    }

    @Override
    public void startSongSession(@NotNull NbsSongSession songSession) {
        this.songSession = Optional.of(songSession);
        songSession.play();
    }

    @Override
    public void startSongSession(@NotNull NbsSong song) {
        startSongSession(new BukkitNbsSongSession(plugin, song, this, Optional.empty()));
    }

    @Override
    public void stopSongSession() {
        songSession.ifPresent(NbsSongSession::stop);
        this.songSession = Optional.empty();
    }
}
