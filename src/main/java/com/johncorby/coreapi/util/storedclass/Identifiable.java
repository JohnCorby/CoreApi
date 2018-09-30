package com.johncorby.coreapi.util.storedclass;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;

/**
 * A class that is identified by another class
 * Can also be used as a wrapper to associate methods/fields/classes with the identities
 */
public abstract class Identifiable<I> extends StoredObject {
    protected I identity;

    public Identifiable(I identity) {
        this.identity = identity;
    }

    public static <I extends Identifiable> I get(@NotNull Class<I> type,
                                                 Object identity) {
        Set<I> is = objects.get(type);
        for (I i : is)
            if (i.get().equals(identity)) return i;
        //throw new IllegalStateException(clazz.getSimpleName() + "<" + identity + "> doesn't exist");
        return null;
    }

    public final I get() {
        assertExists();
        if (identity == null) {
            super.dispose();
            throw new IllegalStateException("Identity for " + this + " unavailable");
        }
        return identity;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "<" + identity + ">";
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifiable<?> that = (Identifiable<?>) o;
        return Objects.equals(identity, that.identity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identity);
    }
}
