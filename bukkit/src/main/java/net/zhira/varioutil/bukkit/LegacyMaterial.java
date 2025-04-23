package net.zhira.varioutil.bukkit;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum LegacyMaterial {
    SKULL("PLAYER_HEAD");

    private static final Map<String, Material> BY_NAME = new HashMap<>();

    private final Material material;
    private final String[] aliases;

    LegacyMaterial(String... aliases) {
        Material material = Material.getMaterial(name());
        if (material == null) {
            for (String name : aliases) {
                material = Material.getMaterial(name);
                if (material != null) break;
            }
            if (material == null) {
                throw new IllegalStateException("Unknown material names: " + Arrays.toString(aliases));
            }
        }
        this.material = material;
        this.aliases = aliases;
    }

    public Material getMaterial() {
        return material;
    }

    public static Material of(String name) {
        Material material = BY_NAME.get(name);
        if (material == null) {
            material = Material.getMaterial(name);
        }
        return material;
    }

    static {
        for (LegacyMaterial material : values()) {
            for (String alias : material.aliases) {
                BY_NAME.put(alias, material.material);
            }
        }
    }
}
