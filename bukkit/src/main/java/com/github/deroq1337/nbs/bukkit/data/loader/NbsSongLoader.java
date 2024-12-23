package com.github.deroq1337.nbs.bukkit.data.loader;

import com.github.deroq1337.nbs.api.Song;
import com.github.deroq1337.nbs.api.SongLoader;
import com.github.deroq1337.nbs.bukkit.exceptions.NbsLoadException;
import com.github.deroq1337.nbs.bukkit.exceptions.OutdatedNbsException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

public class NbsSongLoader implements SongLoader {

    @Override
    public @NotNull List<Song> loadSongs(@NotNull String path) {
        if (path.isEmpty()) {
            throw new NbsLoadException("Path is empty");
        }

        File directory = new File(path);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new NbsLoadException("Path '" + path + "' is no directory");
        }

        return Arrays.stream(directory.listFiles())
                .filter(file -> file != null && !file.isDirectory() && file.getName().endsWith(".nbs"))
                .map(this::loadSong)
                .toList();
    }

    @Override
    public @NotNull Song loadSong(@NotNull File file) {
        try (NbsInputStream inputStream = new NbsInputStream(new FileInputStream(file))) {
            short firstShort = inputStream.readNbsShort();
            if (firstShort != 0) {
                throw new OutdatedNbsException("First two bytes of .nbs should be 0");
            }

            return new Song(
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
                    inputStream.readNbsShort()
            );
        } catch (Exception e) {
            throw new NbsLoadException(e);
        }
    }
}
