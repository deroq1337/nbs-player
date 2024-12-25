package com.github.deroq1337.nbs.bukkit.data.command.subcommands;

import com.github.deroq1337.nbs.api.NbsUser;
import com.github.deroq1337.nbs.bukkit.BukkitNbsSongPlugin;
import com.github.deroq1337.nbs.bukkit.data.command.NbsSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class NbsJoinSubCommand extends NbsSubCommand {

    public NbsJoinSubCommand(@NotNull BukkitNbsSongPlugin plugin) {
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

        Optional<NbsUser> optionalTargetUser = plugin.getUserRegistry().getUser(targetPlayer.getUniqueId());
        if (optionalTargetUser.isEmpty()) {
            player.sendMessage("§cDieser Spieler ist nicht online");
            return;
        }

        NbsUser targetUser = optionalTargetUser.get();
        if (targetUser.getSongSession().isEmpty()) {
            player.sendMessage("§cDieser Spieler hat keine Session");
            return;
        }

        user.joinSongSession(targetUser.getSongSession().get());
        player.sendMessage("§aDu bist der Session von " + targetPlayer.getName() + " beigetreten");
    }
}
