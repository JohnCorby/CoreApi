package com.johncorby.coreapi.util.eventconversation;

import com.johncorby.coreapi.CoreApiPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

import java.util.function.Function;

public abstract class ListenerWithMessage<E extends Event> implements Listener, EventExecutor {
    private final Class<E> event;
    private final EventPriority priority;
    private final boolean ignoreCancelled;
    private final Function<E, Player> recipient;

    public ListenerWithMessage(Class<E> event,
                               Function<E, Player> recipient) {
        this(event, EventPriority.NORMAL, false, recipient);
    }

    public ListenerWithMessage(Class<E> event, EventPriority priority, boolean ignoreCancelled,
                               Function<E, Player> recipient) {
        this.event = event;
        this.priority = priority;
        this.ignoreCancelled = ignoreCancelled;
        this.recipient = recipient;
    }

    public final Class<E> getEvent() {
        return event;
    }

    public final EventPriority getPriority() {
        return priority;
    }

    public boolean isIgnoreCancelled() {
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
        recipient.apply(e).sendRawMessage(execute(e));
    }


    public abstract String execute(E event) throws EventException;
}
