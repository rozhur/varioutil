package org.zhdev.config;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.nodes.Node;
import org.zhdev.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class BukkitYamlConfig extends YamlConfig implements BukkitConfig {
    public BukkitYamlConfig(String key) {
        super(key);
    }

    public BukkitYamlConfig() {}

    @Override
    protected Object constructHandle(Node keyNode, Node valueNode, String key, Object value) {
        if (value instanceof Map) {
            Map<?, ?> raw = (Map<?, ?>) valueNode;
            if (raw.containsKey(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
                Map<String, Object> map = new LinkedHashMap<>(raw.size());
                for (Map.Entry<?, ?> entry : raw.entrySet()) {
                    map.put(String.valueOf(entry.getKey()), entry.getValue());
                }
                return ConfigurationSerialization.deserializeObject(map);
            }
        }
        return super.constructHandle(keyNode, valueNode, key, value);
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

    @Override
    public File load(Plugin plugin, String path) throws ConfigException {
        ClassLoader classLoader = plugin.getClass().getClassLoader();
        InputStream stream = ResourceUtils.getResource(path, classLoader);
        if (stream != null) {
            load(stream);
        }

        File file = load(plugin.getDataFolder().getPath() + File.separatorChar + path);
        if (file.length() == 0) {
            ResourceUtils.saveResource(path, file, classLoader);
            try {
                load(file);
            } catch (IOException e) {
                throw new ConfigException(e);
            }
        }
        return file;
    }

    @Override
    public File load(Plugin plugin) throws ConfigException {
        return load(plugin, key);
    }

    @Override
    public File saveIfEmpty(Plugin plugin, String path) throws ConfigException {
        return saveIfEmpty(plugin.getDataFolder().getPath() + File.separatorChar + path);
    }

    @Override
    public File saveIfEmpty(Plugin plugin) throws ConfigException {
        return saveIfEmpty(plugin, key);
    }
}
