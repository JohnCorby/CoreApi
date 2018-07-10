package com.johncorby.coreapi.util.eventconversation;

import com.johncorby.coreapi.CoreApiPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

public abstract class ListenerWithResult<E extends Event> implements Listener, EventExecutor {
    private final Class<E> event;
    private final EventPriority priority;
    private final boolean ignoreCancelled;

    public ListenerWithResult(Class<E> event) {
        this(event, EventPriority.NORMAL, false);
    }

    public ListenerWithResult(Class<E> event, EventPriority priority, boolean ignoreCancelled) {
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
        Bukkit.getPluginManager().registerEvent(event, this, priority, this, CoreApiPlugin.PLUGIN, ignoreCancelled);
    }

    public final void unregister() {
        HandlerList.unregisterAll(this);
    }

    public final void execute(Listener listener, Event event) throws EventException {
        E e = (E) event;
        if (execute(e)) onSucceed(e);
        else onFail(e);
    }

    public abstract boolean execute(E event) throws EventException;

    public abstract void onSucceed(E event);

    public abstract void onFail(E event);
}
