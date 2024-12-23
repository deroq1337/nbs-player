package com.github.deroq1337.nbs.bukkit.exceptions;

import org.jetbrains.annotations.NotNull;

public class NbsLoadException extends RuntimeException {

    public NbsLoadException(@NotNull String message) {
        super(message);
    }

    public NbsLoadException(@NotNull Throwable t) {
        super(t);
    }
}
