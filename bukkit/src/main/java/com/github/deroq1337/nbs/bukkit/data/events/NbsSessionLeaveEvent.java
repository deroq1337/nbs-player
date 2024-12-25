package com.github.deroq1337.nbs.bukkit.data.events;

import com.github.deroq1337.nbs.api.NbsSongSession;
import com.github.deroq1337.nbs.api.NbsUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
public class NbsSessionLeaveEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final @NotNull NbsUser user;
    private final @NotNull NbsSongSession songSession;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}