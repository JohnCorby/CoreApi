package com.johncorby.coreapi.command;

import com.johncorby.coreapi.CoreApiPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class BaseCommand {
    protected static final String PERM_ADMIN = CoreApiPlugin.PLUGIN.getName() + ".admin";
    private final String description;
    private final String usage;
    private final String permission;

    public BaseCommand(String description, String usage, String permission) {
        this.description = description;
        this.usage = usage;
        this.permission = permission;
    }

    public abstract boolean onCommand(Player sender, String[] args);

    public final String getName() {
        return this.getClass().getSimpleName().toLowerCase();
    }

    public final String getDescription() {
        return description;
    }

    public final String getUsage() {
        return usage;
    }

    public final boolean hasPermission(CommandSender sender) {
        return permission.isEmpty() || sender.isOp() || sender.hasPermission(permission);
    }
}
