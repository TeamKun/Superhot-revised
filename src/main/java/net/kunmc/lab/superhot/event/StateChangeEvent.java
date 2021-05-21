package net.kunmc.lab.superhot.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class StateChangeEvent extends Event {
    private final Class<?> stateClazz;
    private static final HandlerList handlerList = new HandlerList();

    public StateChangeEvent(Class<?> clazz) {
        this.stateClazz = clazz;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public Class<?> getStateClass() {
        return this.stateClazz;
    }
}
