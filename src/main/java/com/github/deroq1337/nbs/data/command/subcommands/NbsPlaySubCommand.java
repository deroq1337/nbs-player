package com.github.deroq1337.nbs.data.command.subcommands;

import com.github.deroq1337.nbs.NbsSongPlugin;
import com.github.deroq1337.nbs.data.command.NbsSubCommand;
import com.github.deroq1337.nbs.data.models.NbsSong;
import com.github.deroq1337.nbs.data.session.NbsSongSession;
import com.github.deroq1337.nbs.data.user.NbsUser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class NbsPlaySubCommand extends NbsSubCommand {

    public NbsPlaySubCommand(@NotNull NbsSongPlugin plugin) {
        super(plugin, "play");
    }

    @Override
    protected void execute(@NotNull Player player, @NotNull NbsUser user, @NotNull String[] args) {
        if (args.length < 1) {
            player.sendMessage("§c/nbs play <song>");
            return;
        }

        StringBuilder songName = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            songName.append(args[i]);
            if (i != args.length - 1) {
                songName.append(" ");
            }
        }

        plugin.getSongManager().getSongByName(songName.toString()).ifPresentOrElse(song -> {
            user.getSongSession().ifPresentOrElse(songSession -> {
                if (!songSession.isOwner(user)) {
                    player.sendMessage("§cDiese Session gehört dir nicht");
                    return;
                }

                if (songSession.isCurrentSong(song)) {
                    player.sendMessage("§cDieses Lied wird bereits abgespielt");
                    return;
                }

                songSession.setCurrentSong(song);
                player.sendMessage("§aLied wird abgespielt");
            }, () -> {
                user.startSongSession(song);
                player.sendMessage("§aLied wird abgespielt");
            });

        }, () -> player.sendMessage("§cDieses Lied wurde nicht gefunden!"));
    }
}