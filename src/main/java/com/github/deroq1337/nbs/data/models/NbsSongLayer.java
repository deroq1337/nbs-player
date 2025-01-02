package com.github.deroq1337.nbs.data.models;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record NbsSongLayer(@NotNull Map<Integer, NbsSongNote> notes) {
}