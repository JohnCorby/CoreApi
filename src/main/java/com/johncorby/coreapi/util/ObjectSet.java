package com.johncorby.coreapi.util;

import com.johncorby.coreapi.util.storedclass.Identifiable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ObjectSet extends AbstractSet<Identifiable> {
    private final Map<Class<? extends Identifiable>, Set<? extends Identifiable>> map = new Hashtable<>();

    @Override
    public Iterator<Identifiable> iterator() {
        Set<Identifiable> s = new HashSet<>();
        map.values().forEach(s::addAll);
        return s.iterator();
    }

    @Override
    public int size() {
        return map.values().stream().mapToInt(Set::size).sum();
    }

    @Override
    public boolean contains(Object o) {
        if (!(o instanceof Identifiable)) return false;
        Identifiable Identifiable = (Identifiable) o;

        for (Map.Entry<Class<? extends Identifiable>, Set<? extends Identifiable>> entry : map.entrySet()) {
            if (entry.getKey() != Identifiable.getClass()) continue;
            return entry.getValue().contains(Identifiable);
        }
        return false;
    }

    @Override
    public boolean add(Identifiable Identifiable) {
        Set<Identifiable> s = get((Class<Identifiable>) Identifiable.getClass());

        boolean ret = s.add(Identifiable);
        if (ret) map.put(Identifiable.getClass(), s);
        return ret;
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof Identifiable)) return false;
        Identifiable Identifiable = (Identifiable) o;

        Set<? extends Identifiable> s = get(Identifiable.getClass());

        boolean ret = s.remove(Identifiable);
        if (ret)
            if (s.isEmpty()) map.remove(Identifiable.getClass());
            else map.put(Identifiable.getClass(), s);
        return ret;
    }

    @Override
    public void clear() {
        map.clear();
    }

    // Get objects of type and objects of subclass of type
    @NotNull
    public <T extends Identifiable> Set<T> get(@NotNull Class<T> type) {
        Set<T> set = new HashSet<>();
        for (Map.Entry<Class<? extends Identifiable>, Set<? extends Identifiable>> entry : map.entrySet())
            if (type.isAssignableFrom(entry.getKey())) set.addAll((Set<T>) entry.getValue());
        return set;
        //Set<T> ret = (Set<T>) map.get(type);
        //return ret == null ? new HashSet<>() : ret;
    }
}
