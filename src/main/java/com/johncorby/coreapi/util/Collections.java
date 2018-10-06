package com.johncorby.coreapi.util;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Collections {
    public static <T, R> List<R> map(@NotNull Collection<T> collection, Function<? super T, ? extends R> function) {
        return collection.stream()
                .map(function)
                .collect(Collectors.toList());
    }

    public static <T> List<T> filter(@NotNull Collection<T> collection, Predicate<? super T> predicate) {
        return collection.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static <T> boolean containsAny(@NotNull Collection<T> collection1, Collection<T> collection2) {
        for (T t : collection2)
            if (collection1.contains(t)) return true;
        return false;
    }

    public static <T, F> List<T> filterByField(Collection<T> collection, Function<T, F> fieldToGet, F fieldToMatch) {
        return collection.stream()
                .filter(t -> Objects.equals(fieldToGet.apply(t), fieldToMatch))
                .collect(Collectors.toList());
    }

    public static <T, S extends T> List<T> filterBySubclass(Collection<T> collection, Class<S> subclass) {
        return collection.stream()
                .filter(t -> t.getClass().isAssignableFrom(subclass))
                .map(t -> (S) t)
                .collect(Collectors.toList());
    }
}
