package com.johncorby.coreapi.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.logging.Level;

import static com.johncorby.coreapi.util.MessageHandler.MessageType.*;

public class MessageHandler {
    private static final String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "VirtualRedstone" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET;

    public static void error(Exception exception) {
        exception.printStackTrace();
    }

    public static boolean playerError(CommandSender to, Object... messages) {
        for (Object m : messages)
            msg(to, ERROR, "Error: " + m);
        return false;
    }

    // Message of type to player
    public static void msg(CommandSender to, MessageType type, Object... messages) {
        msgP(to, type, "", messages);
    }

    public static void msgP(CommandSender to, MessageType type, String prefix, Object... messages) {
        for (Object message : messages) {
            if (message instanceof Object[]) message = Arrays.toString((Object[]) message);
            String string = message.toString();

            StringBuilder stringF = new StringBuilder(MessageHandler.prefix).append(type.get()).append(" ");
            if (!prefix.isEmpty()) stringF.append(prefix).append(" ");
            stringF.append(string);
            //if (!prefix.isEmpty()) stringF.append(type.get()).append(prefix).append(" ");
            //for (String word : string.split(" "))
            //    stringF.append(type.get()).append(word).append(" ");
            //stringF.setLength(Math.max(string.length() - 1, 0));

            string = stringF.toString();
            to.sendMessage(string);
        }
    }

    // Log of type to console
    public static void log(MessageType type, Object... messages) {
        logP(type, "", messages);
    }

    public static void logP(MessageType type, String prefix, Object... messages) {
        msgP(Bukkit.getConsoleSender(), type, prefix, messages);
    }

    // Nice stuff
    public static void info(Object... msgs) {
        log(INFO, msgs);
    }

    public static void warn(Object... msgs) {
        log(WARN, msgs);
    }

    public static void error(Object... msgs) {
        log(ERROR, msgs);
    }

    public static void debug(Object... msgs) {
        log(DEBUG, msgs);
    }

    public static void info(CommandSender to, Object... msgs) {
        msg(to, INFO, msgs);
    }

    public static void warn(CommandSender to, Object... msgs) {
        msg(to, WARN, msgs);
    }

    public static void error(CommandSender to, Object... msgs) {
        msg(to, ERROR, msgs);
    }

    public static void debug(CommandSender to, Object... msgs) {
        msg(to, DEBUG, msgs);
    }

    public enum MessageType {
        INFO(ChatColor.DARK_GRAY, Level.INFO),
        WARN(ChatColor.YELLOW, Level.WARNING),
        ERROR(ChatColor.RED, Level.SEVERE),
        DEBUG(ChatColor.AQUA, Level.FINE);

        private ChatColor color;
        private Level level;

        MessageType(ChatColor color, Level level) {
            this.color = color;
            this.level = level;
        }

        public ChatColor get() {
            return color;
        }

        public Level level() {
            return level;
        }
    }
}
