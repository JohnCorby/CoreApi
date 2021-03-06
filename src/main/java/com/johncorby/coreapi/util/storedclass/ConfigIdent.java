package com.johncorby.coreapi.util.storedclass;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class ConfigIdent<I>
        extends Identifiable<I>
        implements ConfigurationSerializable {
    public ConfigIdent(I identity) {
        super(identity);
    }

    public ConfigIdent(@NotNull Map<String, Object> map) {
        super(null);
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
        return map;
    }

    public void deserialize(@NotNull Map<String, Object> map) {
        identity = (I) map.get("Identity");
    }

    public abstract void configAdd();

    public abstract boolean configRemove();
}
