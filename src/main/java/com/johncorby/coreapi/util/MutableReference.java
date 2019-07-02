package com.johncorby.coreapi.util;

import java.util.Objects;

/**
 * Wraps anything
 * <p>
 * When referent object is changed, all references to this will also change
 * Ensures no creation of new object to heap with separate attributes
 *
 * @param <T> type of object to refer to
 */
public class MutableReference<T> {
    private T referent;

    public MutableReference() {
        this(null);
    }

    public MutableReference(T referent) {
        set(referent);
    }

    public T get() {
        return referent;
    }

    public void set(T referent) {
        this.referent = referent;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MutableReference<?> that = (MutableReference<?>) object;
        return Objects.equals(get(), that.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(get());
    }

    @Override
    public String toString() {
        return "reference of " + get();
    }
}
