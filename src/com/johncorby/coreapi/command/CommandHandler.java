package com.johncorby.coreapi.command;

import com.johncorby.virtualredstone.util.MessageHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.johncorby.virtualredstone.VirtualRedstone.virtualRedstone;
import static com.johncorby.virtualredstone.util.MessageHandler.playerError;
import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;

public class CommandHandler implements CommandExecutor {
    public static List<BaseCommand> commands = new ArrayList<>();

    public CommandHandler() {
        // Register base command
        virtualRedstone.getCommand("virtualredstone").setExecutor(this);

        // Register BaseCommands
        //register(new Help());

        register(new Reload());
        register(new Add());
        register(new Debug());
        register(new SetTableCombo());

        //TabCompleteHandler.register("help", 0, () -> Common.map(commands, BaseCommand::getName));
    }

    private static void register(BaseCommand command) {
        commands.add(command);
    }

    // Get commands
    public static List<BaseCommand> getCommands(CommandSender who) {
        return commands.stream().filter(c -> c.hasPermission(who)).collect(Collectors.toList());
    }

    public static BaseCommand getCommand(String name) {
        for (BaseCommand command : commands)
            if (command.getName().equals(name)) return command;
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        // Change args to lowercase
        args = Arrays.stream(args).map(String::toLowerCase).toArray(String[]::new);

        // If sender not player: say so
        if (!(sender instanceof Player)) return playerError(sender, "Sender must be player");
        Player player = (Player) sender;

        // If no args: show help for all commands
        if (args.length == 0) {
            Help.getHelp(player, commands.toArray(new BaseCommand[0]));
            return false;
        }
        BaseCommand baseCommand = getCommand(args[0]);

        // If command not found or no permission: say so
        if (baseCommand == null || !baseCommand.hasPermission(player))
            return playerError(player, "Command " + args[0] + " not found", "Do /virtualredstone for a list of commands");

        args = Arrays.copyOfRange(args, 1, args.length);

        // Try to execute command or show playerError if playerError
        try {
            return baseCommand.onCommand(player, args);
        } catch (Exception e) {
            playerError(player, e);
            MessageHandler.error((Object) getStackTrace(e));
            return false;
        }
    }
}
