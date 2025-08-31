package org.zhdev.config;

import java.util.*;
import java.util.function.Function;

public class MapConfigSection implements ConfigSection {
    protected final LinkedHashMap<String, ConfigSectionNode> map;

    protected MapConfigSection(LinkedHashMap<String, ConfigSectionNode> map) {
        this.map = map;
    }

    protected MapConfigSection() {
        this(new LinkedHashMap<>());
    }

    public String[] getBlockComments(String key) {
        ConfigSectionNode node = map.get(key);
        return node == null ? null : node.blockComments;
    }

    public String[] getInlineComments(String key) {
        ConfigSectionNode node = map.get(key);
        return node == null ? null : node.inlineComments;
    }

    public void setBlockComments(String key, String... blockComments) {
        ConfigSectionNode node = map.get(key);
        if (node == null) {
            node = new ConfigSectionNode();
            map.put(key, node);
        }
        node.blockComments = blockComments;
    }

    public void setInlineComments(String key, String... inlineComments) {
        ConfigSectionNode node = map.get(key);
        if (node == null) {
            node = new ConfigSectionNode();
            map.put(key, node);
        }
        node.inlineComments = inlineComments;
    }

    public void removeBlockComments(String key) {
        ConfigSectionNode node = map.get(key);
        if (node != null) {
            node.blockComments = null;
        }
    }

    public void removeInlineComments(String key) {
        ConfigSectionNode node = map.get(key);
        if (node != null) {
            node.inlineComments = null;
        }
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>(this.map.size());
        for (Map.Entry<String, ConfigSectionNode> entry : this.map.entrySet()) {
            Object obj = entry.getValue().value;
            if (obj instanceof MapConfigSection) {
                obj = ((MapConfigSection) obj).toMap();
            }
            map.put(entry.getKey(), obj);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        ConfigSectionNode node = map.get(key);
        return node == null ? null : (T) node.value;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, T fallback) {
        ConfigSectionNode node = map.get(key);
        return node == null ? fallback : (T) node.value;
    }

    public <T> T get(String key, Function<String, T> fallback) {
        T object = get(key);
        return object == null ? fallback.apply(key) : object;
    }

    public <T> T getOrSet(String key, T fallback, String... blockComments) {
        T object = get(key);
        if (object == null) {
            map.put(key, new ConfigSectionNode(fallback, blockComments));
            return fallback;
        }
        return object;
    }

    public <T> T getOrSet(String key, Function<String, T> fallback, String... blockComments) {
        T object = get(key);
        if (object == null) {
            object = fallback.apply(key);
            map.put(key, new ConfigSectionNode(object, blockComments));
        }
        return object;
    }

    public MapConfigSection getSection(String key, String... names) {
        Object object = get(key);
        if (object instanceof MapConfigSection) {
            MapConfigSection section = (MapConfigSection) object;
            for (String n : names) {
                section = section.getSection(n);
                if (section == null) return null;
            }
            return section;
        }
        return null;
    }

    public MapConfigSection getOrCreateSection(String key, String... names) {
        MapConfigSection section;
        Object object = get(key);
        if (object instanceof MapConfigSection) {
            section = (MapConfigSection) object;
        } else {
            section = new MapConfigSection();
            map.put(key, new ConfigSectionNode(section));
        }
        for (String n : names) section = section.getOrCreateSection(n);
        return section;
    }
    
    public Byte getByte(String key) {
        Object object = get(key);
        if (object instanceof Byte) {
            return (Byte) object;
        } else if (object instanceof Number) {
            Number number = (Number) object;
            return number.byteValue();
        }
        return null;
    }
    
    public Byte getByte(String key, Byte fallback) {
        Byte object = getByte(key);
        return object == null ? fallback : object;
    }

    public Byte getByte(String key, Function<String, Byte> fallback) {
        Byte object = getByte(key);
        return object == null ? fallback.apply(key) : object;
    }

    public Byte getOrSetByte(String key, Byte fallback, String... blockComments) {
        Byte object = getByte(key);
        if (object == null) {
            map.put(key, new ConfigSectionNode(fallback, blockComments));
            return fallback;
        }
        return object;
    }

    public Byte getOrSetByte(String key, Function<String, Byte> fallback, String... blockComments) {
        Byte object = getByte(key);
        if (object == null) {
            object = fallback.apply(key);
            map.put(key, new ConfigSectionNode(object, blockComments));
        }
        return object;
    }

    public Short getShort(String key) {
        Object object = get(key);
        if (object instanceof Short) {
            return (Short) object;
        } else if (object instanceof Number) {
            Number number = (Number) object;
            return number.shortValue();
        }
        return null;
    }

    public Short getShort(String key, Short fallback) {
        Short object = getShort(key);
        return object == null ? fallback : object;
    }

    public Short getShort(String key, Function<String, Short> fallback) {
        Short object = getShort(key);
        return object == null ? fallback.apply(key) : object;
    }

    public Short getOrSetShort(String key, Short fallback, String... blockComments) {
        Short object = getShort(key);
        if (object == null) {
            map.put(key, new ConfigSectionNode(fallback, blockComments));
            return fallback;
        }
        return object;
    }

    public Short getOrSetShort(String key, Function<String, Short> fallback, String... blockComments) {
        Short object = getShort(key);
        if (object == null) {
            object = fallback.apply(key);
            map.put(key, new ConfigSectionNode(object, blockComments));
        }
        return object;
    }

    public Integer getInteger(String key) {
        Object object = get(key);
        if (object instanceof Integer) {
            return (Integer) object;
        } else if (object instanceof Number) {
            Number number = (Number) object;
            return number.intValue();
        }
        return null;
    }

    public Integer getInteger(String key, Integer fallback) {
        Integer object = getInteger(key);
        return object == null ? fallback : object;
    }

    public Integer getInteger(String key, Function<String, Integer> fallback) {
        Integer object = getInteger(key);
        return object == null ? fallback.apply(key) : object;
    }

    public Integer getOrSetInteger(String key, Integer fallback, String... blockComments) {
        Integer object = getInteger(key);
        if (object == null) {
            map.put(key, new ConfigSectionNode(fallback, blockComments));
            return fallback;
        }
        return object;
    }

    public Integer getOrSetInteger(String key, Function<String, Integer> fallback, String... blockComments) {
        Integer object = getInteger(key);
        if (object == null) {
            object = fallback.apply(key);
            map.put(key, new ConfigSectionNode(object, blockComments));
        }
        return object;
    }

    public Long getLong(String key) {
        Object object = get(key);
        if (object instanceof Long) {
            return (Long) object;
        } else if (object instanceof Number) {
            Number number = (Number) object;
            return number.longValue();
        }
        return null;
    }

    public Long getLong(String key, Long fallback) {
        Long object = getLong(key);
        return object == null ? fallback : object;
    }

    public Long getLong(String key, Function<String, Long> fallback) {
        Long object = getLong(key);
        return object == null ? fallback.apply(key) : object;
    }

    public Long getOrSetLong(String key, Long fallback, String... blockComments) {
        Long object = getLong(key);
        if (object == null) {
            map.put(key, new ConfigSectionNode(fallback, blockComments));
            return fallback;
        }
        return object;
    }

    public Long getOrSetLong(String key, Function<String, Long> fallback, String... blockComments) {
        Long object = getLong(key);
        if (object == null) {
            object = fallback.apply(key);
            map.put(key, new ConfigSectionNode(object, blockComments));
        }
        return object;
    }

    public Float getFloat(String key) {
        Object object = get(key);
        if (object instanceof Float) {
            return (Float) object;
        } else if (object instanceof Number) {
            Number number = (Number) object;
            return number.floatValue();
        }
        return null;
    }

    public Float getFloat(String key, Float fallback) {
        Float object = getFloat(key);
        return object == null ? fallback : object;
    }

    public Float getFloat(String key, Function<String, Float> fallback) {
        Float object = getFloat(key);
        return object == null ? fallback.apply(key) : object;
    }

    public Float getOrSetFloat(String key, Float fallback, String... blockComments) {
        Float object = getFloat(key);
        if (object == null) {
            map.put(key, new ConfigSectionNode(fallback, blockComments));
            return fallback;
        }
        return object;
    }

    public Float getOrSetFloat(String key, Function<String, Float> fallback, String... blockComments) {
        Float object = getFloat(key);
        if (object == null) {
            object = fallback.apply(key);
            map.put(key, new ConfigSectionNode(object, blockComments));
        }
        return object;
    }

    public Double getDouble(String key) {
        Object object = get(key);
        if (object instanceof Double) {
            return (Double) object;
        } else if (object instanceof Number) {
            Number number = (Number) object;
            return number.doubleValue();
        }
        return null;
    }

    public Double getDouble(String key, Double fallback) {
        Double object = getDouble(key);
        return object == null ? fallback : object;
    }

    public Double getDouble(String key, Function<String, Double> fallback) {
        Double object = getDouble(key);
        return object == null ? fallback.apply(key) : object;
    }

    public Double getOrSetDouble(String key, Double fallback, String... blockComments) {
        Double object = getDouble(key);
        if (object == null) {
            map.put(key, new ConfigSectionNode(fallback, blockComments));
            return fallback;
        }
        return object;
    }

    public Double getOrSetDouble(String key, Function<String, Double> fallback, String... blockComments) {
        Double object = getDouble(key);
        if (object == null) {
            object = fallback.apply(key);
            map.put(key, new ConfigSectionNode(object, blockComments));
        }
        return object;
    }
    
    public String getString(String key) {
        Object object = get(key);
        return object == null ? null : object.toString();
    }
    
    public String getString(String key, String fallback) {
        String object = getString(key);
        return object == null ? fallback : object;
    }

    public String getString(String key, Function<String, String> fallback) {
        String object = getString(key);
        return object == null ? fallback.apply(key) : object;
    }

    public String getOrSetString(String key, String fallback, String... blockComments) {
        String object = getString(key);
        if (object == null) {
            map.put(key, new ConfigSectionNode(fallback, blockComments));
            return fallback;
        }
        return object;
    }

    public String getOrSetString(String key, Function<String, String> fallback, String... blockComments) {
        String object = getString(key);
        if (object == null) {
            object = fallback.apply(key);
            map.put(key, new ConfigSectionNode(object, blockComments));
        }
        return object;
    }

    @Override
    public Boolean getBoolean(String key) {
        Object object = get(key);
        if (object instanceof Boolean) {
            return (Boolean) object;
        }
        return object == null ? null : Boolean.getBoolean(object.toString());
    }

    @Override
    public Boolean getBoolean(String key, Boolean fallback) {
        Boolean object = getBoolean(key);
        return object == null ? fallback : object;
    }

    @Override
    public Boolean getBoolean(String key, Function<String, Boolean> fallback) {
        Boolean object = getBoolean(key);
        return object == null ? fallback.apply(key) : object;
    }

    @Override
    public Boolean getOrSetBoolean(String key, Boolean fallback, String... blockComments) {
        Boolean object = getBoolean(key);
        if (object == null) {
            map.put(key, new ConfigSectionNode(fallback, blockComments));
            return fallback;
        }
        return object;
    }

    @Override
    public Boolean getOrSetBoolean(String key, Function<String, Boolean> fallback, String... blockComments) {
        Boolean object = getBoolean(key);
        if (object == null) {
            object = fallback.apply(key);
            map.put(key, new ConfigSectionNode(object, blockComments));
        }
        return object;
    }

    public List<?> getList(String key) {
        Object object = get(key);
        return object instanceof List ? (List<?>) object : null;
    }

    public List<?> getList(String key, List<?> fallback) {
        List<?> object = getList(key);
        return object == null ? fallback : object;
    }

    public List<?> getList(String key, Function<String, List<?>> fallback) {
        List<?> object = getList(key);
        return object == null ? fallback.apply(key) : object;
    }

    public List<?> getOrSetList(String key, List<?> fallback, String... blockComments) {
        List<?> object = getList(key);
        if (object == null) {
            map.put(key, new ConfigSectionNode(fallback, blockComments));
            return fallback;
        }
        return object;
    }

    public List<?> getOrSetList(String key, Function<String, List<?>> fallback, String... blockComments) {
        List<?> object = getList(key);
        if (object == null) {
            object = fallback.apply(key);
            map.put(key, new ConfigSectionNode(fallback, blockComments));
        }
        return object;
    }

    @SuppressWarnings("unchecked")
    public <T> T set(String key, T value, String... blockComments) {
        ConfigSectionNode node = map.get(key);
        if (node == null) {
            return (T) map.put(key, new ConfigSectionNode(value, blockComments));
        }
        Object oldValue = node.value;
        node.value = value;
        node.blockComments = blockComments;
        return (T) oldValue;
    }

    @SuppressWarnings("unchecked")
    public <T> T set(String key, T value) {
        ConfigSectionNode node = map.get(key);
        if (node == null) {
            return (T) map.put(key, new ConfigSectionNode(value));
        }
        Object oldValue = node.value;
        node.value = value;
        return (T) oldValue;
    }

    @SuppressWarnings("unchecked")
    public <T> T remove(String key) {
        return (T) map.remove(key);
    }

    public int size() {
        return map.size();
    }

    public void clear() {
        map.clear();
    }

    @Override
    public Iterator<String> iterator() {
        return map.keySet().iterator();
    }
}
