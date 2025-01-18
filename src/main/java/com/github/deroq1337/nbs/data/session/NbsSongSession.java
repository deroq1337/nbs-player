package com.github.deroq1337.nbs.data.session;

import com.github.deroq1337.nbs.NbsSongPlugin;
import com.github.deroq1337.nbs.data.events.NbsSessionDisbandEvent;
import com.github.deroq1337.nbs.data.events.NbsSongOverEvent;
import com.github.deroq1337.nbs.data.events.NbsSongPlayEvent;
import com.github.deroq1337.nbs.data.models.NbsNotePitch;
import com.github.deroq1337.nbs.data.models.NbsSong;
import com.github.deroq1337.nbs.data.models.NbsSongInstrument;
import com.github.deroq1337.nbs.data.models.NbsSongNote;
import com.github.deroq1337.nbs.data.user.NbsUser;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
public class NbsSongSession {

    private final @NotNull NbsSongPlugin plugin;
    private final @NotNull NbsUser owner;
    private final @NotNull Set<NbsUser> listeningUsers;
    private final Optional<Location> location;

    private Optional<BukkitTask> task = Optional.empty();
    private Optional<NbsSong> currentSong = Optional.empty();
    private boolean playing;

    @Setter
    private int currentTick;

    public NbsSongSession(@NotNull NbsSongPlugin plugin, @NotNull NbsUser owner, @NotNull NbsSong song, Optional<Location> location) {
        this.plugin = plugin;
        this.currentSong = Optional.of(song);
        this.owner = owner;
        this.location = location;
        this.listeningUsers = new HashSet<>();

        join(owner);
    }

    public NbsSongSession(@NotNull NbsSongPlugin plugin, @NotNull NbsUser owner, @NotNull NbsSong song) {
        this(plugin, owner, song, Optional.empty());
    }

    public void play() {
        currentSong.ifPresent(song -> {
            this.playing = true;
            Bukkit.getPluginManager().callEvent(new NbsSongPlayEvent(this, song));

            this.task = Optional.of(new BukkitRunnable() {
                @Override
                public void run() {
                    if (!playing) {
                        return;
                    }

                    if (currentTick > song.length()) {
                        Bukkit.getPluginManager().callEvent(new NbsSongOverEvent(NbsSongSession.this, song));

                        cancel();
                        stop();
                        return;
                    }

                    song.layers().forEach((layerIndex, layer) ->
                            Optional.ofNullable(layer.notes().get(currentTick)).ifPresent(note -> playNote(note)));
                    currentTick++;
                }
            }.runTaskTimer(plugin, 0L, Math.round((float) 20L / song.actualTempo())));
        });
    }

    public void stop() {
        task.ifPresent(BukkitTask::cancel);

        this.currentSong = Optional.empty();
        this.task = Optional.empty();
        this.playing = false;
        this.currentTick = 0;
    }

    public void disband() {
        Bukkit.getPluginManager().callEvent(new NbsSessionDisbandEvent(this));

        stop();
        listeningUsers.stream()
                .filter(user -> !user.equals(owner))
                .forEach(NbsUser::leaveSongSession);
        listeningUsers.clear();
    }

    public void pause() {
        this.playing = false;
    }

    public void resume() {
        this.playing = true;
    }

    public void join(@NotNull NbsUser user) {
        listeningUsers.add(user);
    }

    public void leave(@NotNull NbsUser user) {
        if (user.equals(owner)) {
            disband();
            return;
        }

        listeningUsers.remove(user);
    }

    public void setCurrentSong(@NotNull NbsSong song) {
        if (currentSong.isEmpty() || !song.equals(currentSong.get())) {
            stop();
            this.currentSong = Optional.of(song);
            this.currentTick = 0;
            this.playing = true;
            play();
        }
    }

    public boolean isOwner(@NotNull NbsUser user) {
        return user.equals(owner);
    }

    public boolean isCurrentSong(@NotNull NbsSong song) {
        return currentSong.isPresent() && song.equals(currentSong.get());
    }

    public boolean hasStarted() {
        return task.isPresent();
    }

    private void playNote(@NotNull NbsSongNote note) {
        getListeningUsers().forEach(user -> user.getBukkitPlayer().ifPresent(player ->
                NbsSongInstrument.getInstrumentById(note.instrumentId()).ifPresent(instrument ->
                        player.playSound(location.orElse(player.getLocation()), instrument.getSound(), 1.0f, NbsNotePitch.getPitch(note.key() - 33)))));
    }
}
