package com.johncorby.coreapi.util.storedclass;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class IdentNode<I, P extends IdentNode, C extends IdentNode> extends Identifiable<I> {
    protected final Set<C> children = new HashSet<>();
    @Nullable
    protected P parent = null;

    public IdentNode(I identity, P parent) {
        super(identity);
        create(identity, parent);
    }

//    public IdentNode(I identity, IdentNode... children) {
//        super(identity);
//        create(identity, null, children);
//    }

    protected static <I extends IdentNode> I get(Class<I> clazz,
                                                 Object identity,
                                                 @NotNull IdentNode parent) {
        Set<I> identifiables = classes.get(clazz);
        if (identifiables == null) return null;
        for (I i : identifiables)
            if (i.get().equals(identity) &&
                    i.getParent().equals(parent)) return i;
        //throw new IllegalStateException(String.format("%s<%s, %s> doesn't exist",
        //        clazz.getSimpleName(),
        //        identity,
        //        parent));
        return null;
    }

    protected boolean create(I identity, @Nullable P parent) {
        this.parent = parent;
        if (!super.create(identity)) return false;
        if (parent != null) parent.children.add(this);
        return true;
    }

    @Override
    protected final boolean create(I identity) {
        return true;
    }

    @Override
    public boolean dispose() {
        if (!stored()) return false;
        if (parent != null) parent.children.remove(this);
        children.forEach(IdentNode::dispose);
        return super.dispose();
    }

    @Nullable
    public P getParent() throws IllegalStateException {
        if (!exists())
            throw new IllegalStateException(this + " doesn't exist");
        return parent;
    }

    @NotNull
    public Set<C> getChildren() {
        if (!exists())
            throw new IllegalStateException(this + " doesn't exist");
        return children;
    }

    @Override
    public String toString() {
        return String.format("%s<%s, %s>",
                getClass().getSimpleName(),
                identity,
                parent);
    }

    @Override
    public boolean equals(@NotNull Object obj) {
        if (!getClass().equals(obj.getClass())) return false;
        IdentNode i = (IdentNode) obj;
        return Objects.equals(identity, i.identity) &&
                Objects.equals(parent, i.parent);
    }

    @NotNull
    @Override
    public List<String> getDebug() {
        List<String> r = new ArrayList<>();
        r.add(toString());
        r.add("Children: " + children);
        return r;
    }
}
