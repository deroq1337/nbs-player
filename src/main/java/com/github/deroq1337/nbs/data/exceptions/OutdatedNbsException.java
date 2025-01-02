package com.github.deroq1337.nbs.data.exceptions;

import org.jetbrains.annotations.NotNull;

public class OutdatedNbsException extends RuntimeException {

    public OutdatedNbsException(@NotNull String message) {
        super(message);
    }
}
