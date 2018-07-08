package com.johncorby.coreapi.util.storedclass;

import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.johncorby.coreapi.CoreApiPlugin.messageHandler;
import static com.johncorby.coreapi.util.MessageHandler.MessageType.*;

/**
 * Store classes to manage them
 */
public abstract class StoredClass {
    protected static final ClassSet classes = new ClassSet();
    protected boolean exists = false;

    public StoredClass() throws IllegalStateException {
        create();
    }

    public static ClassSet getClasses() {
        return classes;
    }

    protected boolean create() throws IllegalStateException {
        if (stored()) return false;
        classes.add(this);
        exists = true;
        debug("Created");
        return true;
    }

    public final boolean stored() {
        return classes.contains(this);
    }

    public boolean dispose() {
        if (!stored()) return false;
        classes.remove(this);
        exists = false;
        debug("Disposed");
        return true;
    }

    @Override
    protected void finalize() throws Throwable {
        dispose();
        super.finalize();
    }

    public final void info(Object... msgs) {
        messageHandler.logP(INFO, toString(), msgs);
    }

    public final void warn(Object... msgs) {
        messageHandler.logP(WARN, toString(), msgs);
    }

    public final void error(Object... msgs) {
        messageHandler.logP(ERROR, toString(), msgs);
    }

    public final void debug(Object... msgs) {
        messageHandler.logP(DEBUG, toString(), msgs);
    }

    public List<String> getDebug() {
        List<String> r = new ArrayList<>();
        r.add(toString());

        return r;
    }

    public static final class ClassSet
            extends AbstractSet<StoredClass>
            implements Set<StoredClass> {
        private Map<Class<? extends StoredClass>, Set<? extends StoredClass>> map = new Hashtable<>();

        @NotNull
        @Override
        public Iterator<StoredClass> iterator() {
            Set<StoredClass> s = new HashSet<>();
            map.values().forEach(s::addAll);
            return s.iterator();
        }

        @Override
        public int size() {
            return map.values().stream().mapToInt(Set::size).sum();
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof StoredClass)) return false;
            StoredClass storedClass = (StoredClass) o;

            for (Map.Entry<Class<? extends StoredClass>, Set<? extends StoredClass>> entry : map.entrySet()) {
                if (entry.getKey() != storedClass.getClass()) continue;
                return entry.getValue().contains(storedClass);
            }
            return false;
        }

        @Override
        public boolean add(StoredClass storedClass) {
            Set<StoredClass> s = (Set<StoredClass>) get(storedClass.getClass());
            if (s == null) s = new HashSet<>();

            boolean ret = s.add(storedClass);
            if (ret) map.put(storedClass.getClass(), s);
            return ret;
        }

        @Override
        public boolean remove(Object o) {
            if (!(o instanceof StoredClass)) return false;
            StoredClass storedClass = (StoredClass) o;

            Set<? extends StoredClass> s = map.get(storedClass.getClass());
            if (s == null) return false;

            boolean ret = s.remove(storedClass);
            if (ret)
                if (s.isEmpty()) map.remove(storedClass.getClass());
                else map.put(storedClass.getClass(), s);
            return ret;
        }

        @Override
        public void clear() {
            map.clear();
        }

        public Set<? extends StoredClass> get(Class<? extends StoredClass> clazz) {
            return map.get(clazz);
        }
    }
}

