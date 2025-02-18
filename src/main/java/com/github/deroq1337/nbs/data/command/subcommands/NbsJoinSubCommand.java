package com.github.deroq1337.nbs.data.command.subcommands;

import com.github.deroq1337.nbs.NbsSongPlugin;
import com.github.deroq1337.nbs.data.command.NbsSubCommand;
import com.github.deroq1337.nbs.data.user.NbsUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NbsJoinSubCommand extends NbsSubCommand {

    public NbsJoinSubCommand(@NotNull NbsSongPlugin plugin) {
        super(plugin, "join");
    }

    @Override
    protected void execute(@NotNull Player player, @NotNull NbsUser user, @NotNull String[] args) {
        if (args.length < 1) {
            player.sendMessage("§c/nbs join <user>");
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null || !targetPlayer.isOnline()) {
            player.sendMessage("§cDieser Spieler ist nicht online");
            return;
        }

        if (targetPlayer.equals(player)) {
            player.sendMessage("§cDu kannst deiner eigenen Session nicht beitreten");
            return;
        }

        plugin.getUserRegistry().getUser(targetPlayer.getUniqueId()).ifPresentOrElse(targetUser -> {
            targetUser.getSongSession().ifPresentOrElse(songSession -> {
                user.joinSongSession(songSession);
                player.sendMessage("§aDu bist der Session von " + targetPlayer.getName() + " beigetreten");
            }, () -> player.sendMessage("§cDieser Spieler hat keine Session"));
        }, () -> player.sendMessage("§cDieser Spieler ist nicht online"));
    }
}
