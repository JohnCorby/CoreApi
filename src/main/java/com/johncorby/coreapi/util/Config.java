package com.johncorby.coreapi.util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

import static com.johncorby.coreapi.CoreApiPlugin.PLUGIN;
import static com.johncorby.coreapi.util.Common.toList;
import static com.johncorby.coreapi.util.Common.toSet;

public class Config {
    public static Set<?> getSet(String path) {
        return toSet(PLUGIN.getConfig().getList(path));
    }

    public static void setSet(@NotNull String path, @NotNull Set<?> set) {
        set.removeIf(Objects::isNull);
        PLUGIN.getConfig().set(path, toList(set));
    }

    public static boolean addSet(@NotNull String path, Object object) {
        Set set = getSet(path);
        boolean ret = set.add(object);
        if (ret) setSet(path, set);
        return ret;
    }

    public static boolean removeSet(@NotNull String path, Object object) {
        Set set = getSet(path);
        boolean ret = set.remove(object);
        if (ret) setSet(path, set);
        return ret;
    }
}
