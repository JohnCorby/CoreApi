package com.johncorby.coreapi.util.storedclass;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.johncorby.coreapi.util.Common.*;

public abstract class IdentNode<I, P extends IdentNode, C extends IdentNode>
        extends Identifiable<I> {
    protected P parent;

    public IdentNode(I identity, P parent) {
        super(identity);
        this.parent = parent;
    }

    protected static <T extends IdentNode> T get(@NotNull Class<T> type,
                                                 Object identity,
                                                 @NotNull IdentNode parent) {
        Set<T> is = objects.get(type);
        for (T i : is)
            if (i.get().equals(identity) &&
                    Objects.equals(i.parent, parent)) return i;
        return null;
    }

//    @Override
//    public boolean create() {
//        if (!super.create()) return false;
//        //if (parent != null) parent.children.add(this);
//        return true;
//    }

    @Override
    public boolean dispose() {
        if (!super.dispose()) return false;
        //if (parent != null) parent.children.remove(this);
        getChildren().forEach(IdentNode::dispose);
        return true;
    }


    public P getParent() {
        return parent;
    }


    @NotNull
    public Set<C> getChildren() {
        return toSet(map(filter(objects.get(IdentNode.class), i -> Objects.equals(i.parent, this)), c -> (C) c));
    }

    @Override
    public String toString() {
        return String.format("%s<%s, %s>",
                getClass().getSimpleName(),
                identity,
                parent);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        IdentNode<?, ?, ?> identNode = (IdentNode<?, ?, ?>) o;
        return Objects.equals(parent, identNode.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), parent);
    }

    @Override
    public List<String> getDebug() {
        List<String> r = super.getDebug();
        r.add("Children: " + getChildren());
        return r;
    }
}
