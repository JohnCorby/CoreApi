package com.johncorby.coreapi.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.Runnable;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Common {
    // Convert array to list and vice versa
    public static <T> List<T> toList(T[] o) {
        return Arrays.asList(o);
    }

    public static <T> T[] toArray(List<T> l, @NotNull T[] i) {
        return l.toArray(i);
    }

    // Convert object to string
    public static <T> String toStr(T o) {
        return String.valueOf(o);
    }

    public static <T> String[] toStr(@NotNull T[] oa) {
        return Arrays.stream(oa).map(Common::toStr).toArray(String[]::new);
    }

    // Convert string to int
    public static <T> List<String> toStr(@NotNull List<T> ol) {
        return map(ol, Common::toStr);
    }

    public static <T> Integer toInt(T o) {
        try {
            return Integer.parseInt(toStr(o));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static <T> Integer[] toInt(@NotNull T[] oa) {
        return Arrays.stream(oa).map(Common::toInt).toArray(Integer[]::new);
    }

    public static <T> List<Integer> toInt(@NotNull List<T> ol) {
        return map(ol, Common::toInt);
    }

    // Concat arrays/lists
    @SafeVarargs
    public static <T> T[] concat(@NotNull T[] instance, T[]... arrays) {
        assert arrays.length > 1;
        List<T> result = new ArrayList<>();
        for (T[] array : arrays) {
            result.addAll(Arrays.asList(array));
        }
        return result.toArray(instance);
    }

    @NotNull
    @SafeVarargs
    public static <T> List<T> concat(List<T>... lists) {
        assert lists.length > 1;
        List<T> result = new ArrayList<>();
        for (List<T> list : lists) {
            result.addAll(list);
        }
        return result;
    }

    // Map list/array
    public static <T, R> R[] map(@NotNull T[] array, Function<? super T, ? extends R> function, @NotNull R[] instance) {
        return toArray(Arrays.stream(array).map(function).collect(Collectors.toList()), instance);
    }

    public static <T, R> List<R> map(List<T> list, Function<? super T, ? extends R> function) {
        return list.stream().map(function).collect(Collectors.toList());
    }

    // Get a random int from min to max, inclusive
    public static int randInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    // Gets random element from array/list
    public static <T> T randChoice(T[] array) {
        return array[randInt(0, array.length - 1)];
    }

    public static <T> T randChoice(List<T> array) {
        return array.get(randInt(0, array.size() - 1));
    }

    // Check if string is int
    public static boolean isInt(String s) {
        return toInt(s) != null;
    }

    // String version of array with formatting
    public static <T> String toStr(@NotNull String s, @NotNull T[] replacements) {
        //assert replacements.getClass().isArray();
        return String.format(s, (Object[]) toStr(replacements));
    }

    // Find property R of list E and return E that has property R
    @Nullable
    public static <T, R> T find(T[] array, Function<? super T, ? extends R> function, R element) {
        return find(Arrays.asList(array), function, element);
    }

    public static <T, R> T find(@NotNull List<T> list, Function<? super T, ? extends R> function, R element) {
        int i = map(list, function).indexOf(element);
        return i < 0 ? null : list.get(i);
    }


    // See if one list has an item from another
    public static <T> boolean containsAny(@NotNull Collection<T> collection1, Collection<T> collection2) {
        for (T e : collection2)
            if (collection1.contains(e)) return true;
        return false;
    }

    // Safely run something and catch exceptions
    public static void run(@NotNull Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            MessageHandler.error(e);
        }
    }

    public static <T> T run(@NotNull Callable<T> action) {
        try {
            return action.call();
        } catch (Exception e) {
            MessageHandler.error(e);
            return null;
        }
    }

    // Time something
    public static long time(Runnable action) {
        long t = System.currentTimeMillis();
        action.run();
        return System.currentTimeMillis() - t;
    }

    public static boolean isThrown(@NotNull Runnable action, @NotNull Exception exception) {
        try {
            action.run();
        } catch (Exception e) {
            return e.getClass() == exception.getClass();
        }
        return false;
    }

    // Iterate through items "async" using Runnables
    public static abstract class RunnableIterator<E> extends com.johncorby.coreapi.util.Runnable implements Consumer<E> {
        @NotNull
        private final Iterator<E> iterator;
        private final int itemsPerTick;

        public RunnableIterator(E[] items, int itemsPerTick) {
            this(Arrays.asList(items), itemsPerTick);
        }

        public RunnableIterator(Collection<E> items, int itemsPerTick) {
            iterator = items.iterator();
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
