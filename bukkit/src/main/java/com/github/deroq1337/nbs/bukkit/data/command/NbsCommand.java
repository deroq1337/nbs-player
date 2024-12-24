package com.github.deroq1337.nbs.bukkit.data.command;

import com.github.deroq1337.nbs.api.NbsUser;
import com.github.deroq1337.nbs.bukkit.BukkitNbsSongPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class NbsCommand implements CommandExecutor {

    private final @NotNull BukkitNbsSongPlugin plugin;

    public NbsCommand(@NotNull BukkitNbsSongPlugin plugin) {
        this.plugin = plugin;
        Optional.ofNullable(plugin.getCommand("nbs")).ifPresent(pluginCommand -> pluginCommand.setExecutor(this));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("Du musst ein Spieler sein!");
            return true;
        }

        Optional<NbsUser> optionalUser = plugin.getUserRegistry().getUser(player.getUniqueId());
        if (optionalUser.isEmpty()) {
            player.sendMessage("§cEs ist ein Fehler aufgetreten. Versuch zu rejoinen oder kontaktiere einen Administrator.");
            return true;
        }

        return true;
    }
}
