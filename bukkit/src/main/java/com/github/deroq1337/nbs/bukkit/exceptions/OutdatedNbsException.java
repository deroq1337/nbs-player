package com.github.deroq1337.nbs.bukkit.exceptions;

import org.jetbrains.annotations.NotNull;

public class OutdatedNbsException extends RuntimeException {

    public OutdatedNbsException(@NotNull String message) {
        super(message);
    }
}
