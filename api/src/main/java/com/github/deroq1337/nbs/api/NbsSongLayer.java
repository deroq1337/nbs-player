package com.github.deroq1337.nbs.api;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record NbsSongLayer(@NotNull Map<Integer, NbsSongNote> notes) {
}
