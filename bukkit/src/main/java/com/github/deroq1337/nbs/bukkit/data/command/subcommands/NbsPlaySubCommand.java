package com.github.deroq1337.nbs.bukkit.data.command.subcommands;

import com.github.deroq1337.nbs.api.NbsSong;
import com.github.deroq1337.nbs.api.NbsSongSession;
import com.github.deroq1337.nbs.api.NbsUser;
import com.github.deroq1337.nbs.bukkit.BukkitNbsSongPlugin;
import com.github.deroq1337.nbs.bukkit.data.command.NbsSubCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class NbsPlaySubCommand extends NbsSubCommand {

    public NbsPlaySubCommand(@NotNull BukkitNbsSongPlugin plugin) {
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

        Optional<NbsSong> optionalSong = plugin.getSongManager().getSongByName(songName.toString());
        if (optionalSong.isEmpty()) {
            player.sendMessage("§cDieses Lied wurde nicht gefunden!");
            return;
        }

        Optional<NbsSongSession> optionalSongSession = user.getSongSession();
        if (optionalSongSession.isEmpty()) {
            user.startSongSession(optionalSong.get());
        } else {
            NbsSongSession songSession = optionalSongSession.get();
            if (!songSession.isOwner(user)) {
                player.sendMessage("§cDiese Session gehört dir nicht");
                return;
            }

            NbsSong song = optionalSong.get();
            if (songSession.isCurrentSong(song)) {
                player.sendMessage("§cDieses Lied wird bereits abgespielt");
                return;
            }

            songSession.setCurrentSong(song);
        }
        player.sendMessage("§aLied wird abgespielt");
    }
}