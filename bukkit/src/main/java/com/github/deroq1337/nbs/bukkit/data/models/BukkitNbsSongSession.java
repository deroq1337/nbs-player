package com.github.deroq1337.nbs.bukkit.data.models;

import com.github.deroq1337.nbs.api.NbsSong;
import com.github.deroq1337.nbs.api.NbsSongNote;
import com.github.deroq1337.nbs.api.NbsSongSession;
import com.github.deroq1337.nbs.api.NbsUser;
import com.github.deroq1337.nbs.bukkit.BukkitNbsSongPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public class BukkitNbsSongSession implements NbsSongSession {

    private final @NotNull BukkitNbsSongPlugin plugin;
    private final @NotNull NbsSong song;
    private final @NotNull NbsUser owner;
    private final List<NbsUser> listeningUsers = new ArrayList<>();
    private final Optional<Location> location;

    private Optional<BukkitTask> task;
    private boolean playing;
    @Setter private int currentTick;

    @Override
    public void play() {
        long tickDurationMillis = (1000 / song.actualTempo());

        this.task = Optional.of(Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (currentTick > song.length()) {
                stop();
                return;
            }

            song.layers().forEach((layerIndex, layer) -> {
                Optional.ofNullable(layer.notes().get(currentTick)).ifPresent(this::playNote);
            });
            currentTick++;
        }, 0L, tickDurationMillis / 50L));
    }

    @Override
    public void stop() {
        task.ifPresent(BukkitTask::cancel);
        this.task = Optional.empty();
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
    public boolean hasStarted() {
        return task.isPresent();
    }

    @Override
    public void addListeningUser(@NotNull NbsUser user) {
        listeningUsers.add(user);
    }

    @Override
    public void removeListeningUser(@NotNull NbsUser user) {
        listeningUsers.remove(user);
    }

    private void playNote(@NotNull NbsSongNote note) {
        getListeningUsers().forEach(user -> {
            getPlayer(user).ifPresent(player -> {
                BukkitNbsSongInstrument.getInstrumentById(note.instrumentId()).ifPresent(instrument -> {
                    player.playSound(location.orElse(player.getLocation()), instrument.getSound(), 1.0f, note.actualPitch());
                });
            });
        });
    }

    private Optional<Player> getPlayer(@NotNull NbsUser user) {
        return Optional.ofNullable(Bukkit.getPlayer(user.getUuid()));
    }
}
