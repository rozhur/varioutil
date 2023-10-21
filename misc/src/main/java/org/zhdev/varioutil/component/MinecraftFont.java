package org.zhdev.varioutil.component;

import java.util.LinkedHashMap;
import java.util.Map;

public enum MinecraftFont {
    UNIFORM("minecraft:uniform"),
    ALT("minecraft:alt"),
    DEFAULT("minecraft:default");
    private static final Map<String, MinecraftFont> BY_NAMESPACE = new LinkedHashMap<>();

    private final String value;

    MinecraftFont(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static MinecraftFont byNamespace(String namespace) {
        return BY_NAMESPACE.get(namespace);
    }

    static {
        for (MinecraftFont font : values()) {
            BY_NAMESPACE.put(font.value, font);
        }
    }
}