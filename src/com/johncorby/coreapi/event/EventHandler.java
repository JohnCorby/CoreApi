package com.johncorby.coreapi.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import static com.johncorby.coreapi.CoreApiPlugin.plugin;

public abstract class EventHandler {
    public EventHandler() {
        register();

        //new Any();
    }

    private void register(Listener event) {
        Bukkit.getPluginManager().registerEvents(event, plugin);
    }

    protected abstract void register();
}
