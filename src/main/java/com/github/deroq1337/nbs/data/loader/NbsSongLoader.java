package com.github.deroq1337.nbs.data.loader;

import com.github.deroq1337.nbs.data.exceptions.NbsLoadException;
import com.github.deroq1337.nbs.data.exceptions.OutdatedNbsException;
import com.github.deroq1337.nbs.data.models.NbsSong;
import com.github.deroq1337.nbs.data.models.NbsSongInstrument;
import com.github.deroq1337.nbs.data.models.NbsSongNote;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class NbsSongLoader {

    private final @NotNull File songsDirectory;

    public @NotNull CompletableFuture<List<NbsSong>> loadSongs() {
        if (!songsDirectory.exists()) {
            songsDirectory.mkdirs();
        }

        if (!songsDirectory.isDirectory()) {
            throw new NbsLoadException("Directory '" + songsDirectory + "' is no directory");
        }

        List<CompletableFuture<Optional<NbsSong>>> futures = Arrays.stream(songsDirectory.listFiles())
                .filter(file -> file != null && !file.isDirectory() && file.getName().endsWith(".nbs"))
                .map(this::loadSong)
                .toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList())
                .exceptionally(t -> {
                    System.err.println("Could not load songs: " + t.getMessage());
                    return new ArrayList<>();
                });
    }

    public @NotNull CompletableFuture<Optional<NbsSong>> loadSong(@NotNull File file) {
        return CompletableFuture.supplyAsync(() -> {
            try (NbsInputStream inputStream = new NbsInputStream(new FileInputStream(file))) {
                short firstShort = inputStream.readNbsShort();
                if (firstShort != 0) {
                    throw new OutdatedNbsException("First two bytes of .nbs should be 0");
                }

                NbsSong song = new NbsSong(
                        inputStream.readByte(),
                        inputStream.readByte(),
                        inputStream.readNbsShort(),
                        inputStream.readNbsShort(),
                        inputStream.readString(),
                        inputStream.readString(),
                        inputStream.readString(),
                        inputStream.readString(),
                        inputStream.readNbsShort(),
                        inputStream.readByte() == 1,
                        inputStream.readByte(),
                        inputStream.readByte(),
                        inputStream.readNbsInt(),
                        inputStream.readNbsInt(),
                        inputStream.readNbsInt(),
                        inputStream.readNbsInt(),
                        inputStream.readNbsInt(),
                        inputStream.readString(),
                        inputStream.readByte() == 1,
                        inputStream.readByte(),
                        inputStream.readNbsShort(),
                        new HashMap<>()
                );

                short tick = -1;
                while (true) {
                    short jumpToNextTick = inputStream.readNbsShort();
                    if (jumpToNextTick == 0) {
                        break;
                    }

                    tick += jumpToNextTick;

                    short layer = -1;
                    while (true) {
                        short jumpToNextLayer = inputStream.readNbsShort();
                        if (jumpToNextLayer == 0) {
                            break;
                        }

                        layer += jumpToNextLayer;
                        byte instrumentId = inputStream.readByte();
                        byte key = inputStream.readByte();
                        byte velocity = inputStream.readByte();
                        int panning = Byte.toUnsignedInt(inputStream.readByte());
                        short pitch = inputStream.readNbsShort();

                        if (NbsSongInstrument.getInstrumentById(instrumentId).isEmpty()) {
                            continue;
                        }

                        NbsSongNote note = new NbsSongNote(instrumentId, key, velocity, panning, pitch);
                        song.addNote(layer, tick, note);
                    }
                }

                System.out.println("Loaded song: " + song.name());
                return Optional.of(song);
            } catch (Exception e) {
                throw new NbsLoadException(e);
            }
        }).exceptionally(t -> {
            System.err.println("Could not load song of file '" + file.getName() + "': " + t.getMessage());
            return Optional.empty();
        });
    }
}
