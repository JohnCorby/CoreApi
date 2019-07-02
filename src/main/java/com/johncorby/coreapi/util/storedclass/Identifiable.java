package com.johncorby.coreapi.util.storedclass;

import com.johncorby.coreapi.util.ObjectSet;
import com.johncorby.coreapi.util.PrintObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;

/**
 * A class that is identified by another class
 * Can also be used as a decorator
 */
public abstract class Identifiable<I> implements PrintObject {
    public static final ObjectSet objects = new ObjectSet();
    protected I identity;
    private boolean exists = false;

    public Identifiable(I identity) {
        this.identity = identity;
    }

    public static <I extends Identifiable> I get(@NotNull Class<I> type,
                                                 Object identity) {
        Set<I> is = objects.get(type);
        for (I i : is)
            if (i.get().equals(identity)) return i;
        return null;
    }

    public boolean create() {
        if (exists() || !objects.add(this)) return false;
        exists = true;
        debug("Created");
        return true;
    }

    public boolean dispose() {
        if (!exists() || !objects.remove(this)) return false;
        objects.remove(this);
        exists = false;
        debug("Disposed");
        return true;
    }

    public final boolean exists() {
        return exists;
    }

    protected final void assertExists() {
        if (!exists())
            throw new IllegalStateException(this + " doesn't exist");
    }

    public final I get() {
        assertExists();
        if (identity == null) {
            dispose();
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
