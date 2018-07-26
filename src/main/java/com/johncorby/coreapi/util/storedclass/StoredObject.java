package com.johncorby.coreapi.util.storedclass;

import com.johncorby.coreapi.PrintObject;

import java.util.*;

/**
 * Store classes to manage them
 */
public abstract class StoredObject implements PrintObject {
    private static final ClassSet classes = new ClassSet();
    private boolean exists = false;

    public static ClassSet getClasses() {
        return classes;
    }

    public boolean create() {
        if (stored()) return false;
        classes.add(this);
        exists = true;
        debug("Created");
        return true;
    }

    public boolean dispose() {
        if (!stored()) return false;
        classes.remove(this);
        exists = false;
        debug("Disposed");
        return true;
    }

    public final boolean exists() {
        return exists;
    }

    public final boolean stored() {
        return classes.contains(this);
    }

    @Override
    protected void finalize() throws Throwable {
        dispose();
        super.finalize();
    }

    public static final class ClassSet
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
            if (s == null) return false;

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

        public <O extends StoredObject> Set<O> get(Class<O> clazz) {
            Set<O> os = (Set<O>) map.get(clazz);
            return os == null ? new HashSet<>() : os;
        }
    }
}

