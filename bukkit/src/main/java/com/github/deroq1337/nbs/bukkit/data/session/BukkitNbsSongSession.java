package com.github.deroq1337.nbs.bukkit.data.session;

import com.github.deroq1337.nbs.api.NbsSong;
import com.github.deroq1337.nbs.api.NbsSongNote;
import com.github.deroq1337.nbs.api.NbsSongSession;
import com.github.deroq1337.nbs.api.NbsUser;
import com.github.deroq1337.nbs.bukkit.BukkitNbsSongPlugin;
import com.github.deroq1337.nbs.bukkit.data.models.BukkitNbsNotePitch;
import com.github.deroq1337.nbs.bukkit.data.models.BukkitNbsSongInstrument;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
public class BukkitNbsSongSession implements NbsSongSession {

    private final @NotNull BukkitNbsSongPlugin plugin;
    private final @NotNull NbsUser owner;
    private Optional<NbsSong> currentSong;
    private final Optional<Location> location;
    private final Set<NbsUser> listeningUsers;

    private Optional<BukkitTask> task;
    private boolean playing;

    @Setter
    private int currentTick;

    public BukkitNbsSongSession(@NotNull BukkitNbsSongPlugin plugin, @NotNull NbsUser owner, @NotNull NbsSong song, Optional<Location> location) {
        this.plugin = plugin;
        this.currentSong = Optional.of(song);
        this.owner = owner;
        this.location = location;
        this.listeningUsers = new HashSet<>();

        join(owner);
    }

    public BukkitNbsSongSession(@NotNull BukkitNbsSongPlugin plugin, @NotNull NbsUser owner, @NotNull NbsSong song) {
        this(plugin, owner, song, Optional.empty());
    }

    @Override
    public void play() {
        currentSong.ifPresent(song -> {
            this.playing = true;

            this.task = Optional.of(new BukkitRunnable() {
                @Override
                public void run() {
                    if (!playing) {
                        return;
                    }

                    if (currentTick > song.length()) {
                        cancel();
                        stop();
                        return;
                    }

                    song.layers().forEach((layerIndex, layer) -> {
                        Optional.ofNullable(layer.notes().get(currentTick)).ifPresent(note -> playNote(note));
                    });
                    currentTick++;
                }
            }.runTaskTimer(plugin, 0L, Math.round((float) 20L / song.actualTempo())));
        });
    }

    @Override
    public void stop() {
        task.ifPresent(BukkitTask::cancel);
        this.task = Optional.empty();
        this.playing = false;
        this.currentTick = 0;
        this.currentSong = Optional.empty();
    }

    @Override
    public void disband() {
        stop();
        listeningUsers.stream()
                .filter(user -> !user.equals(owner))
                .forEach(NbsUser::leaveSongSession);
        listeningUsers.clear();
    }

    @Override
    public void pause() {
        this.playing = false;
    }

    @Override
    public void resume() {
        this.playing = true;
    }

    @Override
    public void join(@NotNull NbsUser user) {
        listeningUsers.add(user);
    }

    @Override
    public void leave(@NotNull NbsUser user) {
        if (user.equals(owner)) {
            disband();
            return;
        }

        listeningUsers.remove(user);
    }

    @Override
    public void setCurrentSong(@NotNull NbsSong song) {
        if (currentSong.isEmpty() || !song.equals(currentSong.get())) {
            stop();
            this.currentSong = Optional.of(song);
            this.currentTick = 0;
            this.playing = true;
            play();
        }
    }

    @Override
    public boolean isOwner(@NotNull NbsUser user) {
        return user.equals(owner);
    }

    @Override
    public boolean isCurrentSong(@NotNull NbsSong song) {
        return currentSong.isPresent() && song.equals(currentSong.get());
    }

    @Override
    public boolean hasStarted() {
        return task.isPresent();
    }

    private void playNote(@NotNull NbsSongNote note) {
        getListeningUsers().forEach(user -> {
            getPlayer(user).ifPresent(player -> {
                BukkitNbsSongInstrument.getInstrumentById(note.instrumentId()).ifPresent(instrument -> {
                    player.playSound(location.orElse(player.getLocation()), instrument.getSound(), 1.0f, BukkitNbsNotePitch.getPitch(note.key() - 33));
                });
            });
        });
    }

    private Optional<Player> getPlayer(@NotNull NbsUser user) {
        return Optional.ofNullable(Bukkit.getPlayer(user.getUuid()));
    }
}
