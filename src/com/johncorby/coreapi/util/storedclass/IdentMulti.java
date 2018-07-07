package com.johncorby.coreapi.util.storedclass;

import java.util.Arrays;
import java.util.Set;

/**
 * Identifiable but with multiple identities
 */
public abstract class IdentMulti extends StoredClass {
    protected Object[] identities;

    public IdentMulti(Object... identities) {
        super();
        create(identities);
    }

    protected static IdentMulti get(Class<? extends IdentMulti> clazz,
                                    Object... identities) {
        Set<? extends IdentMulti> identifiables = (Set<? extends IdentMulti>) classes.get(clazz);
        if (identifiables == null) return null;
        for (IdentMulti i : identifiables)
            if (Arrays.equals(i.get(), identities)) return i;
        throw new IllegalStateException(clazz.getSimpleName() + "<" + Arrays.toString(identities) + "> doesn't exist");
    }

    protected boolean create(Object... identities) {
        this.identities = identities;
        return super.create();
    }

    @Override
    protected final boolean create() {
        return true;
    }

    public final Object[] get() throws IllegalStateException {
        if (!exists)
            throw new IllegalStateException(this + " doesn't exist");
        int a = getUnavailable() + 1;
        if (a > 0) {
            super.dispose();
            throw new IllegalStateException("Identity" + a + " for " + this + " unavailable");
        }
        return identities;
    }

    protected int getUnavailable() {
        for (int i = 0; i < identities.length; i++)
            if (identities[i] == null) return i;
        return -1;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "<" + Arrays.toString(identities) + ">";
    }

    @Override
    public boolean equals(Object obj) {
        if (!getClass().equals(obj.getClass())) return false;
        IdentMulti i = (IdentMulti) obj;
        return Arrays.equals(identities, i.identities);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(identities);
    }
}
