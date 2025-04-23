package net.zhira.varioutil.util;

public class EnumUtils {
    private static <T extends Enum<T>> T[] getValues(T eNum) {
        return getValues(eNum.getDeclaringClass());
    }

    private static <T extends Enum<T>> T[] getValues(Class<T> clazz) {
        return clazz.getEnumConstants();
    }

    private static <T extends Enum<T>> T indexOf(T[] values, int index, boolean orLast, boolean orFirst) {
        if (index < 0) {
            return orLast ? values[values.length - 1] : values[0];
        }

        int max = values.length - 1;
        if (index > max) {
            return orFirst ? values[0] : values[max];
        }

        return values[index];
    }

    private static <T extends Enum<T>> T indexOf(T[] values, int index) {
        return indexOf(values, index, false, false);
    }

    public static <T extends Enum<T>> T indexOf(Class<T> clazz, int index, boolean orLast, boolean orFirst) {
        return indexOf(getValues(clazz), index, orLast, orFirst);
    }

    public static <T extends Enum<T>> T indexOf(Class<T> clazz, int index) {
        return indexOf(getValues(clazz), index);
    }

    public static <T extends Enum<T>> T indexOf(T eNum, int index, boolean orLast, boolean orFirst) {
        return indexOf(getValues(eNum), index, orLast, orFirst);
    }

    public static <T extends Enum<T>> T indexOf(T eNum, int index) {
        return indexOf(getValues(eNum), index);
    }

    public static <T extends Enum<T>> T first(Class<T> clazz) {
        return getValues(clazz)[0];
    }

    public static <T extends Enum<T>> T last(Class<T> clazz) {
        T[] values = getValues(clazz);
        return values[values.length - 1];
    }

    public static <T extends Enum<T>> T first(T eNum) {
        return getValues(eNum)[0];
    }

    public static <T extends Enum<T>> T last(T eNum) {
        T[] values = getValues(eNum);
        return values[values.length - 1];
    }

    public static <T extends Enum<T>> T previous(T eNum, boolean orLast) {
        int previous = eNum.ordinal() - 1;
        return previous < 0 ? orLast ? last(eNum) : getValues(eNum)[0] : getValues(eNum)[previous];
    }

    public static <T extends Enum<T>> T next(T eNum, boolean orFirst) {
        T[] values = getValues(eNum);
        int max = values.length - 1;
        int next = eNum.ordinal() + 1;
        return next > max ? (orFirst ? values[0] : values[max]) : values[next];
    }

    public static <T extends Enum<T>> T previous(T eNum) {
        return previous(eNum, false);
    }

    public static <T extends Enum<T>> T next(T eNum) {
        return next(eNum, false);
    }
}
