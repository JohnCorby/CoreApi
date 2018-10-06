package com.johncorby.coreapi.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * ArrayList but doesn't allow null or duplicates
 *
 * @param <E> element type
 */
public class SpecialList<E> extends AbstractList<E> implements List<E> {
    private final List<E> list = new ArrayList<>();

    public SpecialList() {
    }

    public SpecialList(Collection<? extends E> c) {
        addAll(c);
    }

    @Override
    public E get(int index) {
        return list.get(index);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public E set(int index, E element) {
        if (element == null || contains(element)) return null;
        return list.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        if (element == null || contains(element)) return;
        list.add(index, element);
    }

    @Override
    public boolean add(E e) {
        if (e == null || contains(e)) return false;
        return list.add(e);
    }

    @Override
    public E remove(int index) {
        return list.remove(index);
    }
}
