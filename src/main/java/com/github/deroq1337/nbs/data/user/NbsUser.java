package com.github.deroq1337.nbs.data.user;

import com.github.deroq1337.nbs.NbsSongPlugin;
import com.github.deroq1337.nbs.data.events.NbsSessionLeaveEvent;
import com.github.deroq1337.nbs.data.events.NbsSessionStartEvent;
import com.github.deroq1337.nbs.data.models.NbsSong;
import com.github.deroq1337.nbs.data.session.NbsSongSession;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class NbsUser {

    private final @NotNull NbsSongPlugin plugin;
    private final @NotNull UUID uuid;
    private Optional<NbsSongSession> songSession = Optional.empty();

    public static @NotNull NbsUser create(@NotNull NbsSongPlugin plugin, @NotNull UUID uuid) {
        return new NbsUser(plugin, uuid);
    }

    public void joinSongSession(@NotNull NbsSongSession songSession) {
        if (this.songSession.isPresent()) {
            leaveSongSession();
        }

        this.songSession = Optional.of(songSession);
        songSession.join(this);
    }

    public void startSongSession(@NotNull NbsSong song) {
        NbsSongSession session = new NbsSongSession(plugin, this, song);
        joinSongSession(session);
        session.play();

        Bukkit.getPluginManager().callEvent(new NbsSessionStartEvent(this, session, song));
    }

    public void leaveSongSession() {
        songSession.ifPresent(session -> {
            Bukkit.getPluginManager().callEvent(new NbsSessionLeaveEvent(this, session));
            session.leave(this);
        });
        this.songSession = Optional.empty();

    }
}
