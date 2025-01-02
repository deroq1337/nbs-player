package com.github.deroq1337.nbs.data.command.subcommands;

import com.github.deroq1337.nbs.NbsSongPlugin;
import com.github.deroq1337.nbs.data.command.NbsSubCommand;
import com.github.deroq1337.nbs.data.session.NbsSongSession;
import com.github.deroq1337.nbs.data.user.NbsUser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class NbsResumeSubCommand extends NbsSubCommand {

    public NbsResumeSubCommand(@NotNull NbsSongPlugin plugin) {
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