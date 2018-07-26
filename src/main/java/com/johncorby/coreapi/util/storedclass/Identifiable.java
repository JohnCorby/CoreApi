package com.johncorby.coreapi.util.storedclass;

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

    public static <I extends Identifiable> I get(Class<I> type,
                                                 Object identity) {
        Set<I> is = getClasses().get(type);
        for (I i : is)
            if (i.get().equals(identity)) return i;
        //throw new IllegalStateException(clazz.getSimpleName() + "<" + identity + "> doesn't exist");
        return null;
    }

    public final I get() throws IllegalStateException {
        if (!exists())
            throw new IllegalStateException(this + " doesn't exist");
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
    public boolean equals(Object obj) {
        if (!getClass().equals(obj.getClass())) return false;
        Identifiable i = (Identifiable) obj;
        return Objects.equals(identity, i.identity);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(identity);
    }
}
