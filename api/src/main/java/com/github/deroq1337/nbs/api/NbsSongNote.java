package com.github.deroq1337.nbs.api;

public record NbsSongNote(int instrumentId, int key, float velocity, int panning, short pitch) {

    public float actualPitch() {
        return (float) Math.pow(2.0, (key - 45) / 12.0);
    }
}
