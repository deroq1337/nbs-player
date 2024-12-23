package com.github.deroq1337.nbs.api;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a song in the NBS (Note Block Studio) format.
 * This interface defines the structure and properties of a song,
 * including metadata and song-specific settings.
 * <p>
 * For more information about the NBS format, visit:
 * <a href="https://opennbs.org/nbs">NBS Format</a>
 *
 * @param nbsVersion              The version of the new NBS format.
 * @param vanillaInstrumentCount  The count of vanilla instruments used when the song was saved.
 * @param length                  The length of the song in ticks.
 * @param layerCount              The number of layers in the song. A layer represents a group of note blocks.
 *                                Layers can be used to organize different parts of the song.
 * @param name                    The name of the song.
 * @param nbsAuthor               The author of the song.
 * @param originalAuthor          The original author of the song.
 * @param description             The description of the song.
 * @param tempo                   The tempo of the song, multiplied by 100 (e.g., 1225 for 12.25).
 *                                The tempo is measured in ticks per second.
 * @param autoSaving              Whether auto saving has been enabled or not.
 *                                As of NBS version 4 this value is still saved to the file, but no longer used in the program.
 * @param autoSavingDuration      The amount of minutes between each auto save, if enabled.
 *                                As of NBS version 4 this value is still saved to the file, but no longer used in the program.
 * @param timeSignature           The time signature of the song. Valid values range from 2 to 8, where:
 *                                <ul>
 *                                    <li>3 represents 3/4 time</li>
 *                                    <li>4 represents 4/4 time</li>
 *                                </ul>
 * @param minutesSpent            The amount of minutes spent on the project.
 *                                The default is usually 4/4.
 * @param leftClicks              The number of left-clicks performed by the user during the song creation.
 *                                Left-clicks are typically used to add note blocks or make other edits in the editor.
 * @param rightClicks             The number of right-clicks performed by the user during the song creation.
 *                                Right-clicks are typically used for different types of interactions.
 * @param addedNoteBlocksAmount   The amount of times the user has added a note block to the song.
 * @param removedNoteBlocksAmount The amount of times the user has removed a note block from the song.
 * @param importFileName          The name of the imported .mid or .schematic file, if the song was imported from such a file.
 *                                This value contains only the name of the file (e.g., "song.mid" or "structure.schematic"),
 *                                not the path to it.
 * @param looping                 Whether the song is set to loop.
 *                                If true, the song will loop based on the loop settings; otherwise, it will play through once.
 * @param maxLoopCount            The maximum number of times the song will loop. A value of 0 means infinite looping.
 *                                Any other value specifies the number of times the song will loop.
 * @param loopStartTick           The tick position where the song loop starts. The song will loop back to this tick
 *                                after reaching the maximum number of loops or the end of the song.
 * @param layers                  // TODO: doc
 */
public record NbsSong(
        int nbsVersion,
        int vanillaInstrumentCount,
        short length,
        short layerCount,
        @NotNull String name,
        @NotNull String nbsAuthor,
        @NotNull String originalAuthor,
        @NotNull String description,
        short tempo,
        boolean autoSaving,
        int autoSavingDuration,
        int timeSignature,
        int minutesSpent,
        int leftClicks,
        int rightClicks,
        int addedNoteBlocksAmount,
        int removedNoteBlocksAmount,
        @NotNull String importFileName,
        boolean looping,
        int maxLoopCount,
        short loopStartTick,
        @NotNull Map<Integer, NbsSongLayer> layers
) {

    public int lengthInSeconds() {
        return (length / tempo);
    }

    public boolean isInfiniteLooping() {
        return looping && maxLoopCount == 0;
    }

    public void addNote(int layer, int tick, @NotNull NbsSongNote note) {
        NbsSongLayer songLayer = layers.get(layer);
        if (songLayer == null) {
            songLayer = new NbsSongLayer(new HashMap<>());
            layers.put(layer, songLayer);
        }

        songLayer.notes().put(tick, note);
    }
}