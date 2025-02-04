package com.github.deroq1337.nbs.data.tasks;

import com.github.deroq1337.nbs.NbsSongPlugin;
import com.github.deroq1337.nbs.data.events.NbsSongOverEvent;
import com.github.deroq1337.nbs.data.models.NbsNotePitch;
import com.github.deroq1337.nbs.data.models.NbsSong;
import com.github.deroq1337.nbs.data.models.NbsSongInstrument;
import com.github.deroq1337.nbs.data.models.NbsSongNote;
import com.github.deroq1337.nbs.data.session.NbsSongSession;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@RequiredArgsConstructor
public class NbsSongTask extends BukkitRunnable {

    private final @NotNull NbsSongPlugin plugin;
    private final @NotNull NbsSongSession session;
    private final @NotNull NbsSong song;

    public @NotNull BukkitTask start() {
        return runTaskTimer(plugin, 0L, Math.round((float) 20L / song.actualTempo()));
    }

    @Override
    public void run() {
        if (!session.isPlaying()) {
            return;
        }

        int currentTick = session.getCurrentTick();
        if (currentTick > song.length()) {
            Bukkit.getPluginManager().callEvent(new NbsSongOverEvent(session, song));

            cancel();
            session.stop();
            return;
        }

        song.layers().forEach((layerIndex, layer) ->
                Optional.ofNullable(layer.notes().get(currentTick)).ifPresent(note -> playNote(note)));
        session.incrementTick();
    }

    private void playNote(@NotNull NbsSongNote note) {
        session.getListeningUsers().forEach(user -> user.getBukkitPlayer().ifPresent(player ->
                NbsSongInstrument.getInstrumentById(note.instrumentId()).ifPresent(instrument ->
                        player.playSound(session.getLocation().orElse(player.getLocation()), instrument.getSound(), 1.0f, NbsNotePitch.getPitch(note.key() - 33)))));
    }
}
