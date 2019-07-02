package com.johncorby.coreapi.util;

/**
 * Reference but without all the garbage collection crap
 * This prevents the referent from being finalized
 *
 * @param <T> The referent type
 */
public class StrongReference<T> {
    private T referent;

    public StrongReference(T referent) {
        this.referent = referent;
    }

    public T get() {
        return referent;
    }

    public void clear() {
        referent = null;
    }
}
