package com.johncorby.coreapi;

import com.johncorby.virtualredstone.command.CommandHandler;
import com.johncorby.virtualredstone.command.TabCompleteHandler;
import com.johncorby.virtualredstone.event.EventHandler;
import com.johncorby.virtualredstone.util.Config;
import com.johncorby.virtualredstone.util.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * TODO BUGS: https://github.com/JohnCorby/VirtualRedstone/issues?q=is%3Aopen+is%3Aissue+label%3Abug
 * TODO FEATURES: https://github.com/JohnCorby/VirtualRedstone/issues?q=is%3Aopen+is%3Aissue+label%3Afeature
 */
public class VirtualRedstone extends JavaPlugin {
    public static VirtualRedstone virtualRedstone;

    public static FileConfiguration CONFIG;

    // When plugin loaded
    @Override
    public void onLoad() {
        MessageHandler.log(MessageHandler.MessageType.INFO, "VirtualRedstone loaded");
    }

    // When plugin enabled
    @Override
    public void onEnable() {
        // Register ConfigSerializable
        ConfigurationSerialization.registerClass(com.johncorby.virtualredstone.sequencer.Static.class);
        ConfigurationSerialization.registerClass(com.johncorby.virtualredstone.table.Static.class);

        ConfigurationSerialization.registerClass(com.johncorby.virtualredstone.sequencer.Input.class);
        ConfigurationSerialization.registerClass(com.johncorby.virtualredstone.sequencer.Output.class);
        ConfigurationSerialization.registerClass(com.johncorby.virtualredstone.table.Input.class);
        ConfigurationSerialization.registerClass(com.johncorby.virtualredstone.table.Output.class);

        // Init classes
        virtualRedstone = this;
        CONFIG = getConfig();

        new CommandHandler();
        new TabCompleteHandler();
        new EventHandler();
        new Config();

        MessageHandler.log(MessageHandler.MessageType.INFO, "VirtualRedstone enabled");
    }

    // When plugin disabled
    @Override
    public void onDisable() {
        // Stop all VirtualRedstone tasks
        Bukkit.getScheduler().cancelTasks(this);

        // Dispose all Identifiables
        //for (StoredClass c : StoredClass.getClasses())
        //    c.dispose();

        MessageHandler.log(MessageHandler.MessageType.INFO, "VirtualRedstone disabled");
    }
}
