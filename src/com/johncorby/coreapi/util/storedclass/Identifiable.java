package com.johncorby.coreapi.util.storedclass;

import java.util.Objects;
import java.util.Set;

/**
 * A class that is identified by another class
 * Can also be used as a wrapper to associate methods/fields/classes with the identities
 */
public abstract class Identifiable<I> extends StoredClass {
    protected I identity;

    public Identifiable(I identity) {
        super();
        create(identity);
    }

    protected static Identifiable get(Class<? extends Identifiable> clazz,
                                      Object identity) {
        Set<? extends Identifiable> identifiables = (Set<? extends Identifiable>) classes.get(clazz);
        if (identifiables == null) return null;
        for (Identifiable i : identifiables)
            if (i.get().equals(identity)) return i;
        //throw new IllegalStateException(clazz.getSimpleName() + "<" + identity + "> doesn't exist");
        return null;
    }

    protected boolean create(I identity) {
        this.identity = identity;
        return super.create();
    }

    @Override
    protected final boolean create() {
        return true;
    }

    public final I get() throws IllegalStateException {
        if (!exists())
            throw new IllegalStateException(this + " doesn't exist");
        if (!available()) {
            super.dispose();
            throw new IllegalStateException("Identity for " + this + " unavailable");
        }
        return identity;
    }

    protected boolean available() {
        return identity != null;
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
