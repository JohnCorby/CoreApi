package com.johncorby.coreapi.util;

import com.johncorby.coreapi.CoreApiPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.johncorby.coreapi.CoreApiPlugin.messageHandler;
import static com.johncorby.coreapi.CoreApiPlugin.plugin;
import static com.johncorby.coreapi.util.MessageHandler.MessageType.INFO;

public class Config {
    public Config() {
        // Setup
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();

        // Load statics
        for (Object object : get("Statics")) {
            try {
                messageHandler.log(INFO, "Found static " + object + " in config");
            } catch (Exception e) {
                messageHandler.error(e);
            }
        }

        // Load signs
        for (Object object : get("Signs")) {
            try {
                messageHandler.log(INFO, "Found sign " + object + " in config");
            } catch (Exception e) {
                messageHandler.error(e);
            }
        }
    }

    public static Set<Object> get(String path) {
        return new HashSet<>(plugin.getConfig().getList(path));
    }

    public static void set(String path, Set<Object> set) {
        //set.removeIf(Objects::isNull);
        plugin.getConfig().set(path, new ArrayList<>(set));
        plugin.saveConfig();
    }

    public static void add(String path, Object object) {
        Set<Object> set = get(path);
        set.add(object);
        set(path, set);
    }

    public static void remove(String path, Object object) {
        Set<Object> set = get(path);
        set.remove(object);
        set(path, set);
    }

    public static boolean contains(String path, Object object) {
        return get(path).contains(object);
    }
}
