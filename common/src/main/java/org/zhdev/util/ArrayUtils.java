package org.zhdev.util;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.IntFunction;

public class ArrayUtils {
    public static <T> T get(T[] array, int index) {
        return index < array.length ? array[index] : null;
    }

    public static <T> T get(T[] array, int index, T fallback) {
        T t = get(array, index);
        return t == null ? fallback : t;
    }

    public static <T> T get(T[] array, int index, Function<T[], T> fallbackFunction) {
        T t = get(array, index);
        return t == null ? fallbackFunction.apply(array) : t;
    }

    public static <T, R> R[] map(T[] array, IntFunction<R[]> arrayConstructor, Function<T, R> function) {
        R[] mappedArray = arrayConstructor.apply(array.length);
        for (int i = 0; i < array.length; i ++) {
            mappedArray[i] = function.apply(array[i]);
        }
        return mappedArray;
    }

    public static <T> String[] mapToString(T[] array, IntFunction<String[]> arrayConstructor) {
        return map(array, arrayConstructor, String::valueOf);
    }

    public static <T> Byte[] mapToByte(T[] array, IntFunction<Byte[]> arrayConstructor) {
        return map(array, arrayConstructor, NumberUtils::parseByte);
    }

    public static <T> Short[] mapToShort(T[] array, IntFunction<Short[]> arrayConstructor) {
        return map(array, arrayConstructor, NumberUtils::parseShort);
    }

    public static <T> Integer[] mapToInteger(T[] array, IntFunction<Integer[]> arrayConstructor) {
        return map(array, arrayConstructor, NumberUtils::parseInt);
    }

    public static <T> Long[] mapToLong(T[] array, IntFunction<Long[]> arrayConstructor) {
        return map(array, arrayConstructor, NumberUtils::parseLong);
    }

    public static <T> Float[] mapToFloat(T[] array, IntFunction<Float[]> arrayConstructor) {
        return map(array, arrayConstructor, NumberUtils::parseFloat);
    }

    public static <T> Double[] mapToDouble(T[] array, IntFunction<Double[]> arrayConstructor) {
        return map(array, arrayConstructor, NumberUtils::parseDouble);
    }

    public static <C extends Collection<R>, T, R> C mapAndToCollection(T[] array, IntFunction<C> collectionConstructor, Function<T, R> function) {
        C collection = collectionConstructor.apply(array.length);
        for (T t : array) {
            R r = function.apply(t);
            collection.add(r);
        }
        return collection;
    }
}
