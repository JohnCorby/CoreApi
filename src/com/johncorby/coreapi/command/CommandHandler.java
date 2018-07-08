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

import static com.johncorby.coreapi.CoreApiPlugin.messageHandler;
import static com.johncorby.coreapi.CoreApiPlugin.plugin;
import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;

public abstract class CommandHandler implements CommandExecutor {
    public static Set<BaseCommand> commands = new HashSet<>();

    public CommandHandler() {
        // Register base command
        plugin.getCommand(plugin.getName()).setExecutor(this);

        // Register BaseCommands
        register();
        //register(new Help());

        //register(new Reload());
        //register(new Add());
        //register(new Debug());
        //register(new SetTableCombo());

        //TabCompleteHandler.register("help", 0, () -> Common.map(commands, BaseCommand::getName));
    }

    protected abstract void register();

    public void register(BaseCommand command) {
        commands.add(command);
    }

    // Get commands
    public Set<BaseCommand> getCommands(CommandSender who) {
        return commands.stream().filter(c -> c.hasPermission(who)).collect(Collectors.toSet());
    }

    public BaseCommand getCommand(String name) {
        for (BaseCommand command : commands)
            if (command.getName().equals(name)) return command;
        return null;
    }

    private void getHelp(Player sender, BaseCommand... commands) {
        // Header
        if (commands.length == 1)
            messageHandler.msg(sender, MessageHandler.MessageType.INFO, "----- Help for command " + commands[0].getName() + " -----");
        else
            messageHandler.msg(sender, MessageHandler.MessageType.INFO, "----- Help for commands -----");

        // Get help for commands
        for (BaseCommand c : commands) {
            messageHandler.msg(sender, MessageHandler.MessageType.INFO, "/virtualredstone " + c.getName() + " " + c.getUsage() + " - " + c.getDescription());
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        // Change args to lowercase
        args = Arrays.stream(args).map(String::toLowerCase).toArray(String[]::new);

        // If sender not player: say so
        if (!(sender instanceof Player)) {
            messageHandler.error(sender, "Sender must be player");
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
            messageHandler.error(player, "Command " + args[0] + " not found", "Do /virtualredstone for a list of commands");
            return false;
        }

        args = Arrays.copyOfRange(args, 1, args.length);

        // Try to execute command or show playerError if playerError
        try {
            return baseCommand.onCommand(player, args);
        } catch (Exception e) {
            messageHandler.error(player, e, getStackTrace(e));
            return false;
        }
    }
}
