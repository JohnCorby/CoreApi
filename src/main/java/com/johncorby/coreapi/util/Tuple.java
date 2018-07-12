package com.johncorby.coreapi.util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A immutable tuple of 2 objects
 *
 * @param <A> the 1st object
 * @param <B> the 2nd object
 * @see <a href=https://stackoverflow.com/questions/457629/how-to-return-multiple-objects-from-a-java-method>Source</a>
 */
public class Tuple<A, B> {
    public final A a;
    public final B b;

    public Tuple(A a, B b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tuple)) return false;
        Tuple t = (Tuple) obj;
        return Objects.equals(a, t.a) && Objects.equals(b, t.b);
    }

    @NotNull
    @Override
    public String toString() {
        return "Tuple<" + a + ", " + b + ">@" + Integer.toHexString(hashCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
