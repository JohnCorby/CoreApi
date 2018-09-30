package com.johncorby.coreapi.util.storedclass;

import com.johncorby.coreapi.util.PrintObject;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Store objects to manage them
 */
public abstract class StoredObject implements PrintObject {
    public static final ObjectSet objects = new ObjectSet();
    private boolean exists = false;

    public boolean create() {
        if (stored()) return false;
        objects.add(this);
        exists = true;
        debug("Created");
        return true;
    }

    public boolean dispose() {
        if (!stored()) return false;
        objects.remove(this);
        exists = false;
        debug("Disposed");
        return true;
    }

    public final boolean exists() {
        return exists;
    }

    public final boolean stored() {
        return objects.contains(this);
    }

    protected final void assertExists() {
        if (!exists())
            throw new IllegalStateException(this + " doesn't exist");
    }

    public static final class ObjectSet
            extends AbstractSet<StoredObject>
            implements Set<StoredObject> {
        private final Map<Class<? extends StoredObject>, Set<? extends StoredObject>> map = new Hashtable<>();

        @Override
        public Iterator<StoredObject> iterator() {
            Set<StoredObject> s = new HashSet<>();
            map.values().forEach(s::addAll);
            return s.iterator();
        }

        @Override
        public int size() {
            return map.values().stream().mapToInt(Set::size).sum();
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof StoredObject)) return false;
            StoredObject storedObject = (StoredObject) o;

            for (Map.Entry<Class<? extends StoredObject>, Set<? extends StoredObject>> entry : map.entrySet()) {
                if (entry.getKey() != storedObject.getClass()) continue;
                return entry.getValue().contains(storedObject);
            }
            return false;
        }

        @Override
        public boolean add(StoredObject storedObject) {
            Set<StoredObject> s = get((Class<StoredObject>) storedObject.getClass());

            boolean ret = s.add(storedObject);
            if (ret) map.put(storedObject.getClass(), s);
            return ret;
        }

        @Override
        public boolean remove(Object o) {
            if (!(o instanceof StoredObject)) return false;
            StoredObject storedObject = (StoredObject) o;

            Set<? extends StoredObject> s = get(storedObject.getClass());

            boolean ret = s.remove(storedObject);
            if (ret)
                if (s.isEmpty()) map.remove(storedObject.getClass());
                else map.put(storedObject.getClass(), s);
            return ret;
        }

        @Override
        public void clear() {
            map.clear();
        }

        // Get objects of type and objects of subclass of type
        @NotNull
        public <T extends StoredObject> Set<T> get(@NotNull Class<T> type) {
            Set<T> set = new HashSet<>();
            for (Map.Entry<Class<? extends StoredObject>, Set<? extends StoredObject>> entry : map.entrySet())
                if (type.isAssignableFrom(entry.getKey())) set.addAll((Set<T>) entry.getValue());
            return set;
            //Set<T> ret = (Set<T>) map.get(type);
            //return ret == null ? new HashSet<>() : ret;
        }
    }
}

