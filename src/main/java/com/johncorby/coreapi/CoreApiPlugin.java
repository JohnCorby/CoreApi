package com.johncorby.coreapi;

import com.johncorby.coreapi.command.BaseCommand;
import com.johncorby.coreapi.command.CommandHandler;
import com.johncorby.coreapi.command.TabCompleteHandler;
import com.johncorby.coreapi.listener.ListenerHandler;
import com.johncorby.coreapi.util.MessageHandler;
import com.johncorby.coreapi.util.storedclass.StoredObject;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class CoreApiPlugin extends JavaPlugin {
    public static CoreApiPlugin PLUGIN;

    @Override
    public void onEnable() {
        PLUGIN = this;
        new MessageHandler(getMessagePrefix());
        new CommandHandler(getCommands());
        new TabCompleteHandler();
        new ListenerHandler(getListeners());

        MessageHandler.info(PLUGIN.getName() + " enabled");
        //MessageHandler.debug("Resource: " + getClass().getResource('/' + getClass().getName().replace('.', '/') + ".class"));
    }

    @Override
    public void onDisable() {
        for (StoredObject o : StoredObject.objects) o.dispose();
        Bukkit.getScheduler().cancelTasks(this);

        MessageHandler.info(PLUGIN.getName() + " disabled");
    }


    @NotNull
    public abstract String getMessagePrefix();


    @NotNull
    public abstract BaseCommand[] getCommands();


    @NotNull
    public abstract Listener[] getListeners();
}
