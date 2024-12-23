package com.github.deroq1337.nbs.bukkit.data.user;

import com.github.deroq1337.nbs.api.NbsUser;
import com.github.deroq1337.nbs.bukkit.BukkitNbsSongPlugin;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class BukkitNbsUserRegistry {

    private final @NotNull BukkitNbsSongPlugin plugin;
    private final @NotNull Map<UUID, NbsUser> userMap = new ConcurrentHashMap<>();

    public void addUser(@NotNull UUID uuid) {
        userMap.put(uuid, BukkitNbsUser.create(plugin, uuid));
    }

    public void removeUser(@NotNull UUID uuid) {
        userMap.remove(uuid);
    }

    public Optional<NbsUser> getUser(@NotNull UUID uuid) {
        return Optional.ofNullable(userMap.get(uuid));
    }

    public Collection<NbsUser> getUsers() {
        return userMap.values();
    }
}
