package com.github.deroq1337.nbs.data.user;

import com.github.deroq1337.nbs.NbsSongPlugin;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class NbsUserRegistry {

    private final @NotNull NbsSongPlugin plugin;
    private final @NotNull Map<UUID, NbsUser> userMap = new ConcurrentHashMap<>();

    public void addUser(@NotNull UUID uuid) {
        userMap.put(uuid, new NbsUser(plugin, uuid));
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
