package com.github.deroq1337.nbs.bukkit.data.listeners;

import com.github.deroq1337.nbs.api.NbsSong;
import com.github.deroq1337.nbs.api.NbsSongNote;
import com.github.deroq1337.nbs.bukkit.NoteBlockSongPlugin;
import com.github.deroq1337.nbs.bukkit.data.models.NbsSongInstrument;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class PlayerJoinListener implements Listener {

    private final @NotNull NoteBlockSongPlugin plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getSongManager().getSongByName("All I Want For Christmas Is You").ifPresent(song -> {
            play(event.getPlayer(), song);
        });
    }

    public void play(@NotNull Player player, @NotNull NbsSong song) {
        long tickDurationMillis = (1000 / song.actualTempo());
        AtomicInteger currentTick = new AtomicInteger(0);

        Bukkit.getScheduler().runTaskTimer(plugin, task -> {
            int tick = currentTick.getAndIncrement();
            if (tick > song.length()) {
                task.cancel();
                return;
            }

            song.layers().forEach((layerIndex, layer) -> {
                Optional.ofNullable(layer.notes().get(tick)).ifPresent(note -> playNote(player, note));
            });
        }, 0L, tickDurationMillis / 50L);
    }

    private void playNote(@NotNull Player player, @NotNull NbsSongNote note) {
        NbsSongInstrument.getInstrumentById(note.instrumentId()).ifPresent(instrument -> {
            player.playSound(player.getLocation(), instrument.getSound(), 1.0f, note.actualPitch());
        });
    }
}
