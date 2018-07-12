package com.johncorby.coreapi.command;

import com.johncorby.coreapi.util.MessageHandler;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.johncorby.coreapi.CoreApiPlugin.PLUGIN;
import static org.bukkit.Bukkit.getConsoleSender;
import static org.bukkit.Bukkit.getServer;

public class Reload extends BaseCommand {
    Reload() {
        super("Reload the PLUGIN", "", PERM_ADMIN);
    }

    @Override
    public boolean onCommand(@NotNull Player sender, String[] args) {
        MessageHandler.info(sender, "Reloading " + PLUGIN.getName());
        // Reload using console and plugman
        return getServer().dispatchCommand(getConsoleSender(), "plugman reload " + PLUGIN.getName());
    }
}
