package com.github.deroq1337.nbs.bukkit.data.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public enum NbsSongInstrument {

    PIANO(0, Sound.BLOCK_NOTE_BLOCK_HARP),
    DOUBLE_BASS(1, Sound.BLOCK_NOTE_BLOCK_BASEDRUM),
    BASS_DRUM(2, Sound.BLOCK_NOTE_BLOCK_BASS),
    SNARE_DRUM(3, Sound.BLOCK_NOTE_BLOCK_SNARE),
    CLICK(4, Sound.BLOCK_NOTE_BLOCK_HAT),
    GUITAR(5, Sound.BLOCK_NOTE_BLOCK_GUITAR),
    FLUTE(6, Sound.BLOCK_NOTE_BLOCK_FLUTE),
    BELL(7, Sound.BLOCK_NOTE_BLOCK_BELL),
    CHIME(8, Sound.BLOCK_NOTE_BLOCK_CHIME),
    XYLOPHONE(9, Sound.BLOCK_NOTE_BLOCK_XYLOPHONE),
    IRON_XYLOPHONE(10, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE),
    COW_BELL(11, Sound.BLOCK_NOTE_BLOCK_COW_BELL),
    DIDGERIDOO(12, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO),
    BIT(13, Sound.BLOCK_NOTE_BLOCK_BIT),
    BANJO(14, Sound.BLOCK_NOTE_BLOCK_BANJO),
    PLING(15, Sound.BLOCK_NOTE_BLOCK_PLING);

    private final int id;
    private final @NotNull Sound sound;

    public static Optional<NbsSongInstrument> getInstrumentById(int id) {
        return Arrays.stream(values())
                .filter(instrument -> instrument.id == id)
                .findFirst();
    }
}
