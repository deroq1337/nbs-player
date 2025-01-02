package com.github.deroq1337.nbs.data.command;

import com.github.deroq1337.nbs.NbsSongPlugin;
import com.github.deroq1337.nbs.data.user.NbsUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class NbsSubCommand {

    protected final @NotNull NbsSongPlugin plugin;

    @Getter
    private final @NotNull String name;

    protected abstract void execute(@NotNull Player player, @NotNull NbsUser user, @NotNull String[] args);
}
