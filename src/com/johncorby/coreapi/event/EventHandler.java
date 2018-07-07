package com.johncorby.coreapi.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import static com.johncorby.virtualredstone.VirtualRedstone.virtualRedstone;

public class EventHandler {

    public EventHandler() {
        // Register events
        register(new Block());

        //new Any();
    }

    private static void register(Listener event) {
        Bukkit.getPluginManager().registerEvents(event, virtualRedstone);
    }
}
