package com.github.deroq1337.nbs.data.command;

import com.github.deroq1337.nbs.NbsSongPlugin;
import com.github.deroq1337.nbs.data.command.subcommands.*;
import com.github.deroq1337.nbs.data.user.NbsUser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NbsCommand implements CommandExecutor {

    private final @NotNull NbsSongPlugin plugin;
    private final @NotNull Map<String, NbsSubCommand> subCommandMap;

    public NbsCommand(@NotNull NbsSongPlugin plugin) {
        this.plugin = plugin;
        Optional.ofNullable(plugin.getCommand("nbs")).ifPresent(pluginCommand -> pluginCommand.setExecutor(this));

        this.subCommandMap = Stream.of(
                new NbsJoinSubCommand(plugin),
                new NbsLeaveSubCommand(plugin),
                new NbsPauseSubCommand(plugin),
                new NbsPlaySubCommand(plugin),
                new NbsResumeSubCommand(plugin)
        ).collect(Collectors.toMap(subCommand -> subCommand.getName().toLowerCase(), subCommand -> subCommand));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("Du musst ein Spieler sein!");
            return true;
        }

        if (args.length < 1) {
            commandSender.sendMessage("§c/nbs <subcommand>");
            return true;
        }

        String subCommandName = args[0].toLowerCase();

        Optional.ofNullable(subCommandMap.get(subCommandName)).ifPresentOrElse(subCommand ->{
            plugin.getUserRegistry().getUser(player.getUniqueId()).ifPresentOrElse(
                    user -> subCommand.execute(player, user, buildSubCommandArgs(args)),
                    () -> player.sendMessage("§cEs ist ein Fehler aufgetreten. Versuch zu rejoinen oder kontaktiere einen Administrator.")
            );
        }, () -> commandSender.sendMessage("§cBefehl wurde nicht gefunden"));

        return true;
    }

    private @NotNull String[] buildSubCommandArgs(@NotNull String[] args) {
        return Arrays.stream(args)
                .skip(1)
                .toArray(String[]::new);
    }
}
