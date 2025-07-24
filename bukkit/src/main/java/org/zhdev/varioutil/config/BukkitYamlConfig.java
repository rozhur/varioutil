package org.zhdev.varioutil.config;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.yaml.snakeyaml.nodes.Node;

import java.util.LinkedHashMap;
import java.util.Map;

public final class BukkitYamlConfig extends YamlConfig {
    public BukkitYamlConfig(String key) {
        super(key);
    }

    public BukkitYamlConfig() {
        super();
    }

    @Override
    protected Object constructHandle(MapConfigSection section, Node keyNode, Node valueNode, String key, Object value) {
        if (value instanceof Map) {
            Map<?, ?> raw = (Map<?, ?>) value;
            if (raw.containsKey(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
                Map<String, Object> map = new LinkedHashMap<>(raw.size());
                for (Map.Entry<?, ?> entry : raw.entrySet()) {
                    map.put(String.valueOf(entry.getKey()), entry.getValue());
                }
                return ConfigurationSerialization.deserializeObject(map);
            }
        }
        return super.constructHandle(section, keyNode, valueNode, key, value);
    }

    @Override
    protected Node representHandle(String key, Object value) {
        if (value instanceof ConfigurationSerializable) {
            ConfigurationSerializable serializable = (ConfigurationSerializable) value;
            Map<String, Object> values = new LinkedHashMap<>();
            values.put(ConfigurationSerialization.SERIALIZED_TYPE_KEY, ConfigurationSerialization.getAlias(serializable.getClass()));
            values.putAll(serializable.serialize());
            return REPRESENTER.represent(values);
        }
        return super.representHandle(key, value);
    }
}
