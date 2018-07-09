package com.johncorby.coreapi.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import static com.johncorby.coreapi.CoreApiPlugin.PLUGIN;

public class ListenerHandler {
    public ListenerHandler(Listener... listeners) {
        for (Listener listener : listeners)
            register(listener);
        //new Any();
    }

    public static void register(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, PLUGIN);
    }
}
