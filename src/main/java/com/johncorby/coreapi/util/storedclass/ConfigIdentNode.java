package com.johncorby.coreapi.util.storedclass;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class ConfigIdentNode<I, P extends ConfigIdentNode, C extends IdentNode>
        extends IdentNode<I, P, C>
        implements ConfigurationSerializable {
    public ConfigIdentNode(I identity, P parent) {
        super(identity, parent);
    }

    public ConfigIdentNode(@NotNull Map<String, Object> map) {
        super(null, null);
        deserialize(map);
        create();
    }

    @Override
    public boolean create() {
        if (!super.create()) return false;
        configAdd();
        return true;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("Identity", identity);
        map.put("Parent", parent);
        return map;
    }

    public void deserialize(@NotNull Map<String, Object> map) {
        identity = (I) map.get("Identity");
        parent = (P) map.get("Parent");
    }

    public abstract void configAdd();

    public abstract boolean configRemove();
}
