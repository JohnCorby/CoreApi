package com.johncorby.coreapi.util.eventconversation;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.EventExecutor;

import static com.johncorby.coreapi.CoreApiPlugin.PLUGIN;

public abstract class Listener<E extends Event> implements org.bukkit.event.Listener, EventExecutor {
    private final Class<E> event;
    private final EventPriority priority;
    private final boolean ignoreCancelled;

    public Listener(Class<E> event) {
        this(event, EventPriority.NORMAL, false);
    }

    public Listener(Class<E> event, EventPriority priority, boolean ignoreCancelled) {
        this.event = event;
        this.priority = priority;
        this.ignoreCancelled = ignoreCancelled;
    }

    public final Class<E> getEvent() {
        return event;
    }

    public final EventPriority getPriority() {
        return priority;
    }

    public final boolean isIgnoreCancelled() {
        return ignoreCancelled;
    }

    public final void register() {
        unregister();
        Bukkit.getPluginManager().registerEvent(event, this, priority, this, PLUGIN, ignoreCancelled);
    }

    public final void unregister() {
        HandlerList.unregisterAll(this);
    }

    public final void execute(org.bukkit.event.Listener listener, Event event) throws EventException {
        execute((E) event);
    }

    public abstract void execute(E event) throws EventException;
}
