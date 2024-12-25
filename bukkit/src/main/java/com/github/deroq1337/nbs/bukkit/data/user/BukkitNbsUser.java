package com.github.deroq1337.nbs.bukkit.data.user;

import com.github.deroq1337.nbs.api.NbsSong;
import com.github.deroq1337.nbs.api.NbsSongSession;
import com.github.deroq1337.nbs.api.NbsUser;
import com.github.deroq1337.nbs.bukkit.BukkitNbsSongPlugin;
import com.github.deroq1337.nbs.bukkit.data.events.NbsSessionLeaveEvent;
import com.github.deroq1337.nbs.bukkit.data.events.NbsSessionStartEvent;
import com.github.deroq1337.nbs.bukkit.data.session.BukkitNbsSongSession;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
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
    public void joinSongSession(@NotNull NbsSongSession songSession) {
        if (this.songSession.isPresent()) {
            leaveSongSession();
        }

        this.songSession = Optional.of(songSession);
        songSession.join(this);
    }

    @Override
    public void startSongSession(@NotNull NbsSong song) {
        NbsSongSession session = new BukkitNbsSongSession(plugin, this, song);
        joinSongSession(session);
        session.play();

        Bukkit.getPluginManager().callEvent(new NbsSessionStartEvent(this, session, song));
    }

    @Override
    public void leaveSongSession() {
        songSession.ifPresent(session -> {
            Bukkit.getPluginManager().callEvent(new NbsSessionLeaveEvent(this, session));
            session.leave(this);
        });
        this.songSession = Optional.empty();

    }
}
