package com.github.deroq1337.nbs.bukkit.data.command.subcommands;

import com.github.deroq1337.nbs.api.NbsUser;
import com.github.deroq1337.nbs.bukkit.BukkitNbsSongPlugin;
import com.github.deroq1337.nbs.bukkit.data.command.NbsSubCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NbsLeaveSubCommand extends NbsSubCommand {

    public NbsLeaveSubCommand(@NotNull BukkitNbsSongPlugin plugin) {
        super(plugin, "leave");
    }

    @Override
    protected void execute(@NotNull Player player, @NotNull NbsUser user, @NotNull String[] args) {
        if (user.getSongSession().isEmpty()) {
            player.sendMessage("§cDu bist in keiner Session");
            return;
        }

        user.leaveSongSession();
        player.sendMessage("§aDu hast die Session verlassen");
    }
}