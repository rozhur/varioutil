package org.zhdev.varioutil.reflection;

import org.zhdev.varioutil.util.ReflectionUtils;

import java.lang.reflect.Field;

public class FieldSearcher {
    private Class<?> type;
    private Object instance;
    private Class<?> fieldType;
    private String[] fieldNames;

    public FieldSearcher typeOf(String typeName) {
        this.type = ReflectionUtils.getType(typeName);
        return this;
    }

    public FieldSearcher typeOf(String packageName, String typeName) {
        this.type = ReflectionUtils.getType(packageName, typeName);
        return this;
    }

    public FieldSearcher typeOf(String... typeNames) {
        this.type = ReflectionUtils.searchType(typeNames);
        return this;
    }

    public FieldSearcher typeOf(String packageName, String... typeNames) {
        this.type = ReflectionUtils.searchType(new String[] { packageName }, typeNames);
        return this;
    }

    public FieldSearcher type(Class<?> type) {
        this.type = type;
        return this;
    }

    public FieldSearcher instance(Object instance) {
        this.instance = instance;
        return this;
    }

    public FieldSearcher of(Object instance) {
        this.type = instance.getClass();
        this.instance = instance;
        return this;
    }

    public FieldSearcher fieldType(Class<?> fieldType) {
        this.fieldType = fieldType;
        return this;
    }

    public FieldSearcher fieldTypeOf(String... fieldTypeNames) {
        this.fieldType = ReflectionUtils.searchType(fieldTypeNames);
        return this;
    }

    public FieldSearcher fieldOf(String... fieldNames) {
        this.fieldNames = fieldNames;
        return this;
    }

    public Field search() {
        if (type == null) {
            throw new IllegalStateException("Class not defined");
        }

        if (fieldNames == null || fieldNames.length == 0) {
            throw new IllegalStateException("Field names not specified");
        }

        return ReflectionUtils.searchField(type, fieldType, fieldNames);
    }

    public Object get() {
        return ReflectionUtils.getFieldValue(instance, search());
    }

    public <T> T get(Class<T> type) {
        return type.cast(get());
    }
}
