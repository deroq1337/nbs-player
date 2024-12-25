package com.github.deroq1337.nbs.bukkit.data.command;

import com.github.deroq1337.nbs.api.NbsUser;
import com.github.deroq1337.nbs.bukkit.BukkitNbsSongPlugin;
import com.github.deroq1337.nbs.bukkit.data.command.subcommands.*;
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

    private final @NotNull BukkitNbsSongPlugin plugin;
    private final @NotNull Map<String, NbsSubCommand> subCommandMap;

    public NbsCommand(@NotNull BukkitNbsSongPlugin plugin) {
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

        Optional<NbsSubCommand> subCommand = Optional.ofNullable(subCommandMap.get(subCommandName));
        if (subCommand.isEmpty()) {
            commandSender.sendMessage("§cBefehl wurde nicht gefunden");
            return true;
        }

        Optional<NbsUser> optionalUser = plugin.getUserRegistry().getUser(player.getUniqueId());
        if (optionalUser.isEmpty()) {
            player.sendMessage("§cEs ist ein Fehler aufgetreten. Versuch zu rejoinen oder kontaktiere einen Administrator.");
            return true;
        }

        String[] subCommandArgs = Arrays.stream(args).skip(1).toArray(String[]::new);
        subCommand.get().execute(player, optionalUser.get(), subCommandArgs);
        return true;
    }
}
