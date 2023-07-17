package org.zhdev.config;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface ConfigSection extends Iterable<String> {
    int size();

    String[] getBlockComments(String key);

    String[] getInlineComments(String key);

    void setBlockComments(String key, String... blockComments);

    void setInlineComments(String key, String... inlineComments);

    void removeBlockComments(String key);

    void removeInlineComments(String key);

    Map<String, Object> toMap();

    <T> T get(String key);

    <T> T get(String key, T fallback);

    <T> T get(String key, Function<String, T> fallback);

    <T> T getOrSet(String key, T fallback, String... blockComments);

    <T> T getOrSet(String key, Function<String, T> fallback, String... blockComments);

    ConfigSection getSection(String key, String... names);

    ConfigSection getOrCreateSection(String key, String... names);

    Byte getByte(String key);

    Byte getByte(String key, Byte fallback);

    Byte getByte(String key, Function<String, Byte> fallback);

    Byte getOrSetByte(String key, Byte fallback, String... blockComments);

    Byte getOrSetByte(String key, Function<String, Byte> fallback, String... blockComments);

    Short getShort(String key);

    Short getShort(String key, Short fallback);

    Short getShort(String key, Function<String, Short> fallback);

    Short getOrSetShort(String key, Short fallback, String... blockComments);

    Short getOrSetShort(String key, Function<String, Short> fallback, String... blockComments);

    Integer getInteger(String key);

    Integer getInteger(String key, Integer fallback);

    Integer getInteger(String key, Function<String, Integer> fallback);

    Integer getOrSetInteger(String key, Integer fallback, String... blockComments);

    Integer getOrSetInteger(String key, Function<String, Integer> fallback, String... blockComments);

    Long getLong(String key);

    Long getLong(String key, Long fallback);

    Long getLong(String key, Function<String, Long> fallback);

    Long getOrSetLong(String key, Long fallback, String... blockComments);

    Long getOrSetLong(String key, Function<String, Long> fallback, String... blockComments);

    Float getFloat(String key);

    Float getFloat(String key, Float fallback);

    Float getFloat(String key, Function<String, Float> fallback);

    Float getOrSetFloat(String key, Float fallback, String... blockComments);

    Float getOrSetFloat(String key, Function<String, Float> fallback, String... blockComments);

    Double getDouble(String key);

    Double getDouble(String key, Double fallback);

    Double getDouble(String key, Function<String, Double> fallback);

    Double getOrSetDouble(String key, Double fallback, String... blockComments);

    Double getOrSetDouble(String key, Function<String, Double> fallback, String... blockComments);

    String getString(String key);

    String getString(String key, String fallback);

    String getString(String key, Function<String, String> fallback);

    String getOrSetString(String key, String fallback, String... blockComments);

    String getOrSetString(String key, Function<String, String> fallback, String... blockComments);

    Boolean getBoolean(String key);

    Boolean getBoolean(String key, Boolean fallback);

    Boolean getBoolean(String key, Function<String, Boolean> fallback);

    Boolean getOrSetBoolean(String key, Boolean fallback, String... blockComments);

    Boolean getOrSetBoolean(String key, Function<String, Boolean> fallback, String... blockComments);

    List<?> getList(String key);

    List<?> getList(String key, List<?> fallback);

    List<?> getList(String key, Function<String, List<?>> fallback);

    List<?> getOrSetList(String key, List<?> fallback, String... blockComments);

    List<?> getOrSetList(String key, Function<String, List<?>> fallback, String... blockComments);

    <T> T set(String key, T object, String... blockComments);

    <T> T set(String key, T object);

    <T> T remove(String key);

    void clear();
}
