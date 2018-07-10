package com.johncorby.coreapi.command;

import com.johncorby.coreapi.CoreApiPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TabCompleteHandler implements TabCompleter {
    private static Set<TabResult> tabResults = new HashSet<>();

    public TabCompleteHandler() {
        // Register tab completer
        CoreApiPlugin.PLUGIN.getCommand(CoreApiPlugin.PLUGIN.getName()).setTabCompleter(this);
    }

    public static void register(String command, int argPos, Supplier<Set<String>> results) {
        TabResult tabResult = new TabResult(command, argPos, results);
        if (tabResults.contains(tabResult))
            throw new IllegalArgumentException("TabResult already exists");
        tabResults.add(tabResult);
    }

    public static void register(String command, int argPos, String... results) {
        TabResult tabResult = new TabResult(command, argPos, () -> new HashSet<>(Arrays.asList(results)));
        if (tabResults.contains(tabResult))
            throw new IllegalArgumentException("TabResult already exists");
        tabResults.add(tabResult);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        Set<String> results = TabResult.getResults(args);
        // If no BaseCommand, match BaseCommands
        if (results == null) {
            return new ArrayList<>(TabResult.match(args[0], CommandHandler.getCommands(sender).stream().map(BaseCommand::getName).collect(Collectors.toSet())));
        }

        // Return matching TabResult
        return new ArrayList<>(TabResult.match(args[args.length - 1], results));
    }

    private static class TabResult {
        private String command;
        private int argPos;
        private Supplier<Set<String>> results;
        //private List<String> results;

        private TabResult(String command, int argPos, Supplier<Set<String>> results) {
            this.command = command;
            this.argPos = argPos;
            this.results = results;
        }

        // Returns null if no BaseCommand or results of TabResult that matches
        private static Set<String> getResults(String[] args) {
            if (args.length < 2) return null;
            for (TabResult t : tabResults)
                if (t.command.equals(args[0]) && t.argPos == args.length - 2) return t.results.get();
            return new HashSet<>();
        }

        // Match partial to from
        private static Set<String> match(String partial, Set<String> from) {
            if (from.isEmpty() || partial.isEmpty()) return from;

            Set<String> matches = new HashSet<>();
            for (String s : from)
                if (s.indexOf(partial) == 0) matches.add(s);
            return matches;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof TabResult &&
                    ((TabResult) obj).command.equals(command) &&
                    ((TabResult) obj).argPos == argPos;
        }
    }
}

/*
public class TabCompleteHandler  {
    public static void register(BaseCommand command, int argPos, String... result) {
        error("TabComplete registering won't do anything");
    }
}
*/
