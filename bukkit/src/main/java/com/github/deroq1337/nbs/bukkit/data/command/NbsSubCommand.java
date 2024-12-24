package com.github.deroq1337.nbs.bukkit.data.command;

import com.github.deroq1337.nbs.api.NbsUser;
import com.github.deroq1337.nbs.bukkit.BukkitNbsSongPlugin;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class NbsSubCommand {

    protected final @NotNull BukkitNbsSongPlugin plugin;
    protected final @NotNull String name;

    protected abstract void execute(@NotNull NbsUser user, String[] args);
}
