package net.zhira.varioutil.reflection;

import net.zhira.varioutil.util.ReflectionUtils;

import java.lang.reflect.Method;

public class MethodSearcher {
    private Class<?> type;
    private Object instance;
    private Class<?> returnType;
    private String[] methodNames;
    private Class<?>[] parameterTypes;
    private Object[] args;

    public MethodSearcher typeOf(String typeName) {
        this.type = ReflectionUtils.getType(typeName);
        return this;
    }

    public MethodSearcher typeOf(String packageName, String typeName) {
        this.type = ReflectionUtils.getType(packageName, typeName);
        return this;
    }

    public MethodSearcher typeOf(String... typeNames) {
        this.type = ReflectionUtils.searchType(typeNames);
        return this;
    }

    public MethodSearcher typeOf(String packageName, String... typeNames) {
        this.type = ReflectionUtils.searchType(new String[] { packageName }, typeNames);
        return this;
    }

    public MethodSearcher type(Class<?> clazz) {
        this.type = clazz;
        return this;
    }

    public MethodSearcher instance(Object instance) {
        this.instance = instance;
        return this;
    }

    public MethodSearcher of(Object instance) {
        this.type = instance.getClass();
        this.instance = instance;
        return this;
    }

    public MethodSearcher returns(Class<?> returnType) {
        this.returnType = returnType;
        return this;
    }

    public MethodSearcher returnsOf(String... returnTypeNames) {
        this.returnType = ReflectionUtils.searchType(returnTypeNames);
        return this;
    }

    public MethodSearcher methodOf(String... methodNames) {
        this.methodNames = methodNames;
        return this;
    }

    public MethodSearcher parameters(Class<?>... parameterTypes) {
        this.parameterTypes = parameterTypes;
        return this;
    }

    public MethodSearcher args(Object... args) {
        this.args = args;
        return this;
    }

    public Method search() {
        if (type == null) {
            throw new IllegalStateException("Class not defined");
        }

        if (methodNames == null) {
            methodNames = new String[0];
        }

        if (parameterTypes == null) {
            parameterTypes = new Class[0];
        }

        if (args == null) {
            args = new Object[0];
        }

        return ReflectionUtils.searchMethod(type, returnType, methodNames, parameterTypes);
    }

    public Object invoke() {
        return ReflectionUtils.invokeMethod(instance, search(), args);
    }

    public <T> T invoke(Class<T> returnType) {
        return returnType.cast(invoke());
    }
}
