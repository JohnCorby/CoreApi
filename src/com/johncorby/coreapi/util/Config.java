package com.johncorby.coreapi.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.johncorby.virtualredstone.VirtualRedstone.CONFIG;
import static com.johncorby.virtualredstone.VirtualRedstone.virtualRedstone;
import static com.johncorby.virtualredstone.util.MessageHandler.MessageType.INFO;

public class Config {
    public Config() {
        // Setup
        virtualRedstone.getConfig().options().copyDefaults(true);
        virtualRedstone.saveConfig();

        // Load statics
        for (Object object : get("Statics")) {
            try {
                MessageHandler.log(INFO, "Found static " + object + " in config");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Load signs
        for (Object object : get("Signs")) {
            try {
                MessageHandler.log(INFO, "Found sign " + object + " in config");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Set<Object> get(String path) {
        return new HashSet<>(CONFIG.getList(path));
    }

    public static void set(String path, Set<Object> set) {
        //set.removeIf(Objects::isNull);
        CONFIG.set(path, new ArrayList<>(set));
        virtualRedstone.saveConfig();
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
