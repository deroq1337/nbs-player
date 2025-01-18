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
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class NbsUser {

    private final @NotNull NbsSongPlugin plugin;
    private final @NotNull UUID uuid;

    private Optional<NbsSongSession> songSession = Optional.empty();

    public void joinSongSession(@NotNull NbsSongSession session) {
        if (songSession.isPresent()) {
            leaveSongSession();
        }

        this.songSession = Optional.of(session);
        session.join(this);
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

    public Optional<Player> getBukkitPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(uuid));
    }
}
