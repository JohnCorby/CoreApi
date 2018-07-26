package com.johncorby.coreapi.util.storedclass;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.util.HashMap;
import java.util.Map;

public abstract class ConfigIdent<I> extends Identifiable<I> implements ConfigurationSerializable {
    public ConfigIdent(I identity) {
        super(identity);
        ConfigurationSerialization.registerClass(getClass());
    }

    public ConfigIdent(Map<String, Object> map) {
        super(null);
        deserialize(map);
    }

    @Override
    public boolean create() {
        if (!super.create()) return false;
        configAdd();
        return true;
    }

    @Override
    public boolean dispose() {
        if (!super.dispose()) return false;
        configRemove();
        return true;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("Identity", get());
        return map;
    }

    public void deserialize(Map<String, Object> map) {
        identity = (I) map.get("Identity");
    }

    public abstract void configAdd();

    public abstract void configRemove();
}
