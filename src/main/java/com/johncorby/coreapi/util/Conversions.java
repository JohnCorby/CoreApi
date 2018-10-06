package com.johncorby.coreapi.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Conversions {
    // Convert lists/sets
    public static <T> List<T> toList(T[] ta) {
        return Arrays.asList(ta);
    }

    public static <T> List<T> toList(@NotNull Collection<T> tc) {
        return new ArrayList<>(tc);
    }

    public static <T> Set<T> toSet(T[] ta) {
        return toSet(toList(ta));
    }

    public static <T> Set<T> toSet(@NotNull Collection<T> tc) {
        return new HashSet<>(tc);
    }

    // Convert T to string
    public static <T> String toStr(T t) {
        return String.valueOf(t);
    }

    public static <T> String[] toStr(T[] ta) {
        return toStr(toList(ta)).toArray(new String[0]);
    }

    public static <T> Collection<String> toStr(@NotNull Collection<T> tc) {
        return Collections.map(tc, Conversions::toStr);
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

    public static <T> Collection<Integer> toInt(@NotNull Collection<T> tc) {
        return Collections.map(tc, Conversions::toInt);
    }
}
