package com.johncorby.coreapi.util.storedclass;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class IdentNode<I, P extends IdentNode, C extends IdentNode> extends Identifiable<I> {
    protected Set<C> children = new HashSet<>();
    protected P parent;

    public IdentNode(I identity, P parent) {
        super(identity);
        this.parent = parent;
    }

//    public IdentNode(I identity, IdentNode... children) {
//        super(identity);
//        create(identity, null, children);
//    }

    protected static <I extends IdentNode> I get(Class<I> type,
                                                 Object identity,
                                                 IdentNode parent) {
        Set<I> is = getClasses().get(type);
        for (I i : is)
            if (i.get().equals(identity) &&
                    i.getParent().equals(parent)) return i;
        //throw new IllegalStateException(String.format("%s<%s, %s> doesn't exist",
        //        clazz.getSimpleName(),
        //        identity,
        //        parent));
        return null;
    }

    @Override
    public boolean create() {
        if (!super.create()) return false;
        if (parent != null) parent.children.add(this);
        return true;
    }

    @Override
    public boolean dispose() {
        if (!super.dispose()) return false;
        if (parent != null) parent.children.remove(this);
        children.forEach(IdentNode::dispose);
        return true;
    }


    public P getParent() throws IllegalStateException {
        if (!exists())
            throw new IllegalStateException(this + " doesn't exist");
        return parent;
    }


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
    public boolean equals(Object obj) {
        if (!getClass().equals(obj.getClass())) return false;
        IdentNode i = (IdentNode) obj;
        return Objects.equals(identity, i.identity) &&
                Objects.equals(parent, i.parent);
    }


    @Override
    public List<String> getDebug() {
        List<String> r = super.getDebug();
        r.add("Children: " + children);
        return r;
    }
}
