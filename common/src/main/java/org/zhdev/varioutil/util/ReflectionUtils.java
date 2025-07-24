package org.zhdev.varioutil.util;

import org.zhdev.varioutil.reflection.FieldSearcher;
import org.zhdev.varioutil.reflection.MethodSearcher;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReflectionUtils {
    private static Method getMethod0(Class<?> type, String methodName, Class<?>... args) throws NoSuchMethodException {
        Method method = type.getDeclaredMethod(methodName, args);
        method.setAccessible(true);
        return method;
    }

    private static Field getField0(Class<?> type, String fieldName) throws NoSuchFieldException {
        Field field = type.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    private static boolean compareParameters(Class<?>[] required, Class<?>... parameters) {
        if (required.length != parameters.length) {
            return false;
        }

        for (int i = 0; i < required.length; i++) {
            if (!parameters[i].isAssignableFrom(required[i])) {
                return false;
            }
        }

        return true;
    }

    private static Method checkMethodType(Method method, Class<?> requiredType) throws IllegalStateException {
        if (requiredType == null || requiredType.isAssignableFrom(method.getReturnType())) return method;
        throw new IllegalStateException(requiredType + " is not assignable from type of " + method);
    }

    private static Field checkFieldType(Field field, Class<?> requiredType) throws IllegalStateException {
        if (requiredType == null || requiredType.isAssignableFrom(field.getType())) return field;
        throw new IllegalStateException(requiredType + " is not assignable from type of " + field);
    }

    public static Class<?> getType(String typeName) throws NoClassDefFoundError {
        try {
            return Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(typeName);
        }
    }

    public static Class<?> searchType(String... typeNames) throws NoClassDefFoundError {
        for (String typeName : typeNames) {
            try {
                return Class.forName(typeName);
            } catch (ClassNotFoundException ignored) {
            }
        }

        throw new NoClassDefFoundError(String.join(", ", typeNames));
    }

    public static Class<?> getType(String packageName, String typeSimpleName) throws NoClassDefFoundError {
        String typeName = packageName + '.' + typeSimpleName;
        try {
            return Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(typeName);
        }
    }

    public static Class<?> searchType(String[] packageNames, String... typeNames) throws NoClassDefFoundError {
        for (String packageName : packageNames) {
            for (String typeName : typeNames) {
                try {
                    return Class.forName(packageName + '.' + typeName);
                } catch (ClassNotFoundException ignored) {
                }
            }
        }

        throw new NoClassDefFoundError(String.join(", ", typeNames) + " in " + String.join(", ", packageNames));
    }

    public static Method getMethod(Class<?> type, String methodName, Class<?>... parameterTypes) throws NoSuchMethodError {
        try {
            return getMethod0(type, methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodError(e.getMessage());
        }
    }

    public static Method searchMethod(Class<?> type, Class<?> returnType, String[] methodNames, Class<?>... parameterTypes) throws NoSuchMethodError {
        IllegalStateException exception = null;
        for (String methodName : methodNames) {
            try {
                return checkMethodType(getMethod0(type, methodName, parameterTypes), returnType);
            } catch (IllegalStateException e) {
                exception = e;
            } catch (NoSuchMethodException ignored) {
            }
        }

        if (parameterTypes.length > 0) {
            for (Method m : type.getDeclaredMethods()) {
                if (compareParameters(parameterTypes, m.getParameterTypes())) {
                    return checkMethodType(m, returnType);
                }
            }
        }

        if (exception != null) {
            throw exception;
        }

        throw new NoSuchMethodError(String.join(", ", methodNames) + " in " + type);
    }

    public static Object invokeMethod(Object instance, Method method, Object... args) throws IllegalStateException {
        try {
            return method.invoke(instance, args);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public static Field getField(Class<?> type, String fieldName) throws NoSuchFieldError {
        try {
            return getField0(type, fieldName);
        } catch (NoSuchFieldException e) {
            throw new NoSuchFieldError(fieldName);
        }
    }

    public static Field searchField(Class<?> type, Class<?> fieldType, String... fieldNames) throws NoSuchFieldError {
        IllegalStateException exception = null;
        for (String fieldName : fieldNames) {
            try {
                return checkFieldType(getField0(type, fieldName), fieldType);
            } catch (IllegalStateException e) {
                exception = e;
            } catch (NoSuchFieldException ignored) {
            }
        }

        if (exception != null) {
            throw exception;
        }

        throw new NoSuchFieldError(String.join(", ", fieldNames) + " in " + type);
    }

    public static Object getFieldValue(Object instance, Field field) throws IllegalStateException {
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public static void setFieldValue(Object instance, Field field, Object value) throws IllegalStateException {
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public static void forEachClass(ClassLoader loader, JarFile jarFile, String packageName, Consumer<Class<?>> consumer) {
        for (Enumeration<JarEntry> entry = jarFile.entries(); entry.hasMoreElements(); ) {
            JarEntry jarEntry = entry.nextElement();
            String name = jarEntry.getName();
            if (!name.endsWith(".class")) {
                continue;
            }

            String link = name.substring(0, name.length() - 6).replace("/", ".");

            int index = link.lastIndexOf('.');
            String packagePath = index == -1 ? "" : link.substring(0, index);

            if (packagePath.equals(packageName)) {
                try {
                    consumer.accept(loader.loadClass(link));
                } catch (ClassNotFoundException ignored) {
                }
            }
        }
    }

    public static void forEachClass(ClassLoader loader, File file, String packageName, Consumer<Class<?>> consumer) {
        try (JarFile jarFile = new JarFile(file)) {
            forEachClass(loader, jarFile, packageName, consumer);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void forEachJarEntry(JarFile jarFile, String packageName, Consumer<JarEntry> consumer) {
        for (Enumeration<JarEntry> entry = jarFile.entries(); entry.hasMoreElements(); ) {
            JarEntry jarEntry = entry.nextElement();

            String name = jarEntry.getName();
            int index = name.lastIndexOf('/');
            String packagePath = index == -1 ? name : name.substring(0, index);

            if (packagePath.equals(packageName)) {
                consumer.accept(jarEntry);
            }
        }
    }

    public static void forEachJarEntry(File file, String packageName, Consumer<JarEntry> consumer) {
        try (JarFile jarFile = new JarFile(file)) {
            forEachJarEntry(jarFile, packageName, consumer);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static MethodSearcher methodSearcher() {
        return new MethodSearcher();
    }

    public static FieldSearcher fieldSearcher() {
        return new FieldSearcher();
    }
}
