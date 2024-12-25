package com.github.deroq1337.nbs.bukkit.data.command;

import com.github.deroq1337.nbs.api.NbsUser;
import com.github.deroq1337.nbs.bukkit.BukkitNbsSongPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class NbsSubCommand {

    protected final @NotNull BukkitNbsSongPlugin plugin;

    @Getter
    private final @NotNull String name;

    protected abstract void execute(@NotNull Player player, @NotNull NbsUser user, @NotNull String[] args);
}
