package com.johncorby.coreapi.util;

import java.lang.Runnable;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Common {
    // Convert lists/sets/arrays
    public static <T> List<T> toList(T[] ta) {
        return Arrays.asList(ta);
    }

    public static <T> List<T> toList(Collection<T> tc) {
        return new ArrayList<>(tc);
    }

    public static <T> List<T> toList(Stream<T> ts) {
        return ts.collect(Collectors.toList());
    }

    public static <T> Set<T> toSet(T[] ta) {
        return toSet(toList(ta));
    }

    public static <T> Set<T> toSet(Collection<T> tc) {
        return new HashSet<>(tc);
    }

    public static <T> Set<T> toSet(Stream<T> ts) {
        return ts.collect(Collectors.toSet());
    }

    public static <T> T[] toArray(T[] type, Collection<T> tc) {
        return tc.toArray(type);
    }

    public static <T> T[] toArray(T[] type, Stream<T> ts) {
        return toList(ts).toArray(type);
    }

    public static <T> Stream<T> toStream(T[] ta) {
        return Arrays.stream(ta);
    }

    public static <T> Stream<T> toStream(Collection<T> tc) {
        return tc.stream();
    }


    // Convert T to string
    public static <T> String toStr(T t) {
        return String.valueOf(t);
    }

    public static <T> String[] toStr(T[] ta) {
        return toStr(toList(ta)).toArray(new String[0]);
    }

    public static <T> Collection<String> toStr(Collection<T> tc) {
        return map(tc, Common::toStr);
    }


    // Convert T to int
    public static <T> Integer toInt(T t) {
        try {
            return Integer.parseInt(toStr(t));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static <T> Integer[] toInt(T[] ta) {
        return toInt(toList(ta)).toArray(new Integer[0]);
    }

    public static <T> Collection<Integer> toInt(Collection<T> tc) {
        return map(tc, Common::toInt);
    }


    // Map collection/array
    public static <T, R> R[] map(R[] type, T[] array, Function<? super T, ? extends R> function) {
        return toArray(type, map(toList(array), function));
    }

    public static <T, R> Collection<R> map(Collection<T> collection, Function<? super T, ? extends R> function) {
        return toList(toStream(collection).map(function));
    }


    // Filter collection/array
    public static <T> T[] filter(T[] array, Predicate<? super T> predicate) {
        return toArray(array, filter(toList(array), predicate));
    }

    public static <T> Collection<T> filter(Collection<T> collection, Predicate<? super T> predicate) {
        return toList(toStream(collection).filter(predicate));
    }


    // Find property R of list T and return T that has property R
    public static <T, R> T find(T[] array, Function<? super T, ? extends R> function, R element) {
        return find(toList(array), function, element);
    }

    public static <T, R> T find(List<T> list, Function<? super T, ? extends R> function, R element) {
        int i = toList(map(list, function)).indexOf(element);
        return i < 0 ? null : list.get(i);
    }


    // See if one collection has an element from another
    public static <T> boolean containsAny(T[] array1, T[] array2) {
        return containsAny(toList(array1), toList(array2));
    }

    public static <T> boolean containsAny(Collection<T> collection1, Collection<T> collection2) {
        for (T t : collection2)
            if (collection1.contains(t)) return true;
        return false;
    }


    // Get a random int from min to max, inclusive
    public static int randInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }


    // Gets random element from array/list
    public static <T> T randChoice(T[] array) {
        return array[randInt(0, array.length - 1)];
    }

    public static <T> T randChoice(List<T> list) {
        return list.get(randInt(0, list.size() - 1));
    }


    // Concat arrays/collections
    @SafeVarargs
    public static <T> T[] concat(T[] type, T[]... arrays) {
        assert arrays.length > 1;
        Collection<T> result = new ArrayList<>();
        for (T[] array : arrays)
            result.addAll(toList(array));
        return result.toArray(type);
    }

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
    public static <T> String format(String s, T... replacements) {
        return String.format(s, (Object[]) replacements);
    }


    // Safely run something and catch/print exceptions
    public static void run(Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            MessageHandler.error(e);
        }
    }

    public static <T> T run(Callable<T> action) {
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


    // Check if an exception is thrown
    public static boolean isThrown(Runnable action, Exception exception) {
        try {
            action.run();
        } catch (Exception e) {
            return e.getClass() == exception.getClass();
        }
        return false;
    }


    // Iterate through items "async" using Runnables
    public static abstract class RunnableIterator<T> extends com.johncorby.coreapi.util.Runnable implements Consumer<T> {
        private final Iterator<T> iterator;
        private final int itemsPerTick;

        public RunnableIterator(T[] elements, int itemsPerTick) {
            this(toList(elements), itemsPerTick);
        }

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
