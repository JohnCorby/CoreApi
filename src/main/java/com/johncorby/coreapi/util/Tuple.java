package com.johncorby.coreapi.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return Objects.equals(a, tuple.a) &&
                Objects.equals(b, tuple.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    @NotNull
    @Override
    public String toString() {
        return "Tuple<" + a + ", " + b + ">@" + Integer.toHexString(hashCode());
    }
}
