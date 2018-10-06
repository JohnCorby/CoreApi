package com.johncorby.coreapi.util;

import org.jetbrains.annotations.NotNull;

import java.lang.Runnable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;

public class Misc {


    // Get a random int from min to max, inclusive
    public static int randInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }


    // Gets random element from array/list
    public static <T> T randChoice(List<T> list) {
        return list.get(randInt(0, list.size() - 1));
    }


    @NotNull
    @SafeVarargs
    public static <T> Collection<T> concat(Collection<T>... collections) {
        assert collections.length > 1;
        Collection<T> result = new ArrayList<>();
        for (Collection<T> collection : collections) {
            result.addAll(collection);
        }
        return result;
    }


    // String version of array with formatting
    @SafeVarargs
    public static <T> String format(@NotNull String s, T... replacements) {
        return String.format(s, (Object[]) replacements);
    }


    // Safely run something and catch/print exceptions
    public static void run(@NotNull Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            MessageHandler.error(getStackTrace(e));
        }
    }

    public static <T> T run(@NotNull Callable<T> action) {
        try {
            return action.call();
        } catch (Exception e) {
            MessageHandler.error(getStackTrace(e));
            return null;
        }
    }


    // Time something
    public static long time(Runnable action) {
        long t = System.currentTimeMillis();
        action.run();
        return System.currentTimeMillis() - t;
    }


    // Check if an exception is thrown
    public static boolean isThrown(@NotNull Runnable action, @NotNull Class<? extends Exception> exception) {
        try {
            action.run();
        } catch (Exception e) {
            return e.getClass() == exception;
        }
        return false;
    }


    // Iterate through items "async" using Runnables
    public static abstract class RunnableIterator<T> extends com.johncorby.coreapi.util.Runnable implements Consumer<T> {
        @NotNull
        private final Iterator<T> iterator;
        private final int itemsPerTick;

        public RunnableIterator(Collection<T> elements, int itemsPerTick) {
            iterator = elements.iterator();
            this.itemsPerTick = itemsPerTick;
        }

        public final void go() {
            runTaskTimer(0, 0);
        }

        @Override
        public final void run() {
            for (int i = 0; i < itemsPerTick; i++) {
                if (!iterator.hasNext()) {
                    cancel();
                    return;
                }
                accept(iterator.next());
            }
        }
    }
}
