package com.github.deroq1337.nbs.bukkit.data.command.subcommands;

import com.github.deroq1337.nbs.api.NbsSongSession;
import com.github.deroq1337.nbs.api.NbsUser;
import com.github.deroq1337.nbs.bukkit.BukkitNbsSongPlugin;
import com.github.deroq1337.nbs.bukkit.data.command.NbsSubCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class NbsResumeSubCommand extends NbsSubCommand {

    public NbsResumeSubCommand(@NotNull BukkitNbsSongPlugin plugin) {
        super(plugin, "resume");
    }

    @Override
    protected void execute(@NotNull Player player, @NotNull NbsUser user, @NotNull String[] args) {
        Optional<NbsSongSession> optionalSongSession = user.getSongSession();
        if (optionalSongSession.isEmpty()) {
            player.sendMessage("§cDu bist in keiner Session");
            return;
        }

        NbsSongSession songSession = optionalSongSession.get();
        if (!songSession.isOwner(user)) {
            player.sendMessage("§cDiese Session gehört dir nicht");
            return;
        }

        if (songSession.isPlaying()) {
            player.sendMessage("§cDie Session läuft bereits");
            return;
        }

        songSession.resume();
        player.sendMessage("§aDie Session läuft nun weiter");
    }
}