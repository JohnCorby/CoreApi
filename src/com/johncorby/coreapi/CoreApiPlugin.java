package com.johncorby.coreapi;

import com.johncorby.coreapi.command.CommandHandler;
import com.johncorby.coreapi.command.TabCompleteHandler;
import com.johncorby.coreapi.event.EventHandler;
import com.johncorby.coreapi.util.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CoreApiPlugin extends JavaPlugin {
    public static JavaPlugin plugin;
    public static CommandHandler commandHandler;
    public static TabCompleteHandler tabCompleteHandler;
    public static EventHandler eventHandler;
    public static MessageHandler messageHandler;

    @Override
    public void onLoad() {
        plugin = this;
        register();
        tabCompleteHandler = new TabCompleteHandler();

        messageHandler.info(plugin.getName() + " loaded");
    }

    @Override
    public void onEnable() {
        messageHandler.info(plugin.getName() + " enabled");
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);

        messageHandler.info(plugin.getName() + " disabled");
    }

    public abstract void register();
}
