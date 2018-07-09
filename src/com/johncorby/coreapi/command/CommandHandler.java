package com.johncorby.coreapi.command;

import com.johncorby.coreapi.util.MessageHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.johncorby.coreapi.CoreApiPlugin.PLUGIN;
import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;

public class CommandHandler implements CommandExecutor {
    public static Set<BaseCommand> commands = new HashSet<>();

    public CommandHandler(BaseCommand... baseCommands) {
        // Register base command
        PLUGIN.getCommand(PLUGIN.getName()).setExecutor(this);

        // Register BaseCommands
        register(new Reload());
        register(new Debug());
        for (BaseCommand command : baseCommands)
            register(command);
    }

    public static void register(BaseCommand command) {
        commands.add(command);
    }

    // Get commands
    public static Set<BaseCommand> getCommands(CommandSender who) {
        return commands.stream().filter(c -> c.hasPermission(who)).collect(Collectors.toSet());
    }

    public static BaseCommand getCommand(String name) {
        for (BaseCommand command : commands)
            if (command.getName().equals(name)) return command;
        return null;
    }

    private static void getHelp(Player sender, BaseCommand... commands) {
        // Filter out non-perm commands
        commands = Arrays.stream(commands).filter(baseCommand -> baseCommand.hasPermission(sender)).toArray(BaseCommand[]::new);

        // Header
        MessageHandler.info(sender, "----- Help for commands -----");

        // Get help for commands
        for (BaseCommand c : commands) {
            MessageHandler.info(sender, "/virtualredstone " + c.getName() + " " + c.getUsage() + " - " + c.getDescription());
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        // Change args to lowercase
        args = Arrays.stream(args).map(String::toLowerCase).toArray(String[]::new);

        // If sender not player: say so
        if (!(sender instanceof Player)) {
            MessageHandler.error(sender, "Sender must be player");
            return false;
        }
        Player player = (Player) sender;

        // If no args: show help for all commands
        if (args.length == 0) {
            getHelp(player, commands.toArray(new BaseCommand[0]));
            return false;
        }
        BaseCommand baseCommand = getCommand(args[0]);

        // If command not found or no permission: say so
        if (baseCommand == null || !baseCommand.hasPermission(player)) {
            MessageHandler.error(player, "Command " + args[0] + " not found", "Do /virtualredstone for a list of commands");
            return false;
        }

        args = Arrays.copyOfRange(args, 1, args.length);

        // Try to execute command or show playerError if playerError
        try {
            return baseCommand.onCommand(player, args);
        } catch (Exception e) {
            MessageHandler.error(player, e);
            MessageHandler.error(getStackTrace(e));
            return false;
        }
    }
}
