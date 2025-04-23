package net.zhira.varioutil.util;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public class CollectionUtils {
    public static <T> T get(List<T> list, int index) {
        return index < list.size() ? list.get(index) : null;
    }

    public static <T> T get(List<T> list, int index, T fallback) {
        T t = get(list, index);
        return t == null ? fallback : t;
    }

    public static <T> T get(List<T> list, int index, Supplier<T> fallbackFunction) {
        T t = get(list, index);
        return t == null ? fallbackFunction.get() : t;
    }

    public static  <C extends Collection<R>, T, R> C map(Collection<T> collection, IntFunction<C> collectionConstructor, Function<T, R> function) {
        C mappedCollection = collectionConstructor.apply(collection.size());
        for (T t : collection) {
            mappedCollection.add(function.apply(t));
        }
        return mappedCollection;
    }

    public static <C extends Collection<String>, T> C mapToString(Collection<T> collection, IntFunction<C> collectionConstructor) {
        return map(collection, collectionConstructor, String::valueOf);
    }

    public static <C extends Collection<Byte>, T> C mapToByte(Collection<T> collection, IntFunction<C> collectionConstructor) {
        return map(collection, collectionConstructor, StringUtils::parseByte);
    }

    public static <C extends Collection<Short>, T> C mapToShort(Collection<T> collection, IntFunction<C> collectionConstructor) {
        return map(collection, collectionConstructor, StringUtils::parseShort);
    }

    public static <C extends Collection<Integer>, T> C mapToInteger(Collection<T> collection, IntFunction<C> collectionConstructor) {
        return map(collection, collectionConstructor, StringUtils::parseInt);
    }

    public static <C extends Collection<Long>, T> C mapToLong(Collection<T> collection, IntFunction<C> collectionConstructor) {
        return map(collection, collectionConstructor, StringUtils::parseLong);
    }

    public static <C extends Collection<Float>, T> C mapToFloat(Collection<T> collection, IntFunction<C> collectionConstructor) {
        return map(collection, collectionConstructor, StringUtils::parseFloat);
    }

    public static <C extends Collection<Double>, T> C mapToDouble(Collection<T> collection, IntFunction<C> collectionConstructor) {
        return map(collection, collectionConstructor, StringUtils::parseDouble);
    }
    
    public static <T, R> R[] mapAndToArray(Collection<T> collection, IntFunction<R[]> arrayConstructor, Function<T, R> function) {
        R[] array = arrayConstructor.apply(collection.size());
        int i = 0;
        for (T t : collection) {
            array[i] = function.apply(t);
            i++;
        }
        return array;
    }
}
