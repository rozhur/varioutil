package org.zhdev.varioutil.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StringUtils {
    public static String join(int beginIndex, int lastIndex, String delimiter, String lastDelimiter, String... elements) {
        StringBuilder builder = new StringBuilder();
        for (; beginIndex < elements.length; beginIndex++) {
            if (builder.length() > 0) {
                if (beginIndex == lastIndex) {
                    builder.append(lastDelimiter);
                } else {
                    builder.append(delimiter);
                }
            }
            builder.append(elements[beginIndex]);
        }
        return builder.toString();
    }

    public static String join(int beginIndex, String delimiter, String lastDelimiter, String... elements) {
        return join(beginIndex, elements.length - 1, delimiter, lastDelimiter, elements);
    }

    public static String join(String delimiter, String lastDelimiter, String... elements) {
        return join(0, delimiter, lastDelimiter, elements);
    }

    public static String join(int beginIndex, int lastIndex, String delimiter, String... elements) {
        return join(beginIndex, lastIndex, delimiter, delimiter, elements);
    }

    public static String join(int beginIndex, String delimiter, String... elements) {
        return join(beginIndex, delimiter, delimiter, elements);
    }

    public static String join(String delimiter, String... elements) {
        return join(delimiter, delimiter, elements);
    }

    public static String join(int beginIndex, int lastIndex, String delimiter, String lastDelimiter, Iterable<String> elements) {
        StringBuilder builder = new StringBuilder();
        Iterator<String> iterator = elements.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            if (i < beginIndex) {
                continue;
            }
            String element = iterator.next();
            if (builder.length() > 0) {
                if (iterator.hasNext()) {
                    builder.append(delimiter);
                } else {
                    builder.append(lastDelimiter);
                }
            }
            builder.append(element);
            i++;
            if (i == lastIndex) {
                break;
            }
        }
        return builder.toString();
    }

    public static String join(int beginIndex, String delimiter, String lastDelimiter, Iterable<String> elements) {
        return join(beginIndex, -1, delimiter, lastDelimiter, elements);
    }

    public static String join(String delimiter, String lastDelimiter, Iterable<String> elements) {
        return join(0, delimiter, lastDelimiter, elements);
    }

    public static String join(int beginIndex, int lastIndex, String delimiter, Iterable<String> elements) {
        return join(beginIndex, lastIndex, delimiter, delimiter, elements);
    }

    public static String join(int beginIndex, String delimiter, Iterable<String> elements) {
        return join(beginIndex, delimiter, delimiter, elements);
    }

    public static String join(String delimiter, Iterable<String> elements) {
        return join(delimiter, delimiter, elements);
    }

    public static void replaceKeyValue(StringBuilder builder, Object... args) {
        Object replacement = null;
        for (int i = 0; i < args.length; i++) {
            if (i % 2 == 0) {
                replacement = args[i];
            } else {
                replace(builder, String.valueOf(replacement), args[i]);
            }
        }
    }

    /**
     * <blockquote>For example,
     * <pre>{@code
     *     StringUtils.replaceKeyValue("Hi, %username%! This is %what%",
     *                                 "%username%", "human",
     *                                 "%what%", "Java program")
     * }</pre></blockquote>
     */
    public static String replaceKeyValue(String str, Object... args) {
        if (args.length == 0) {
            return str;
        }
        StringBuilder builder = new StringBuilder(str);
        replaceKeyValue(builder, args);
        return builder.toString();
    }

    public static void replace(StringBuilder builder, String replacement, Object value) {
        int index = builder.indexOf(replacement);
        if (index == -1) {
            return;
        }

        String result = String.valueOf(value);

        do {
            builder.replace(index, index + replacement.length(), result);

            index = builder.indexOf(replacement, index + result.length());
        } while (index != -1);
    }

    public static String escape(String str, String escape, char... chars) {
        StringBuilder builder = new StringBuilder(str);
        for (char c : chars) {
            replace(builder, String.valueOf(c), escape + c);
        }
        return builder.toString();
    }

    public static String escape(String str, char... chars) {
        return escape(str, "\\", chars);
    }

    public static String unescape(String str, String escape, char... chars) {
        StringBuilder builder = new StringBuilder(str);
        for (char c : chars) {
            replace(builder, escape + c, c);
        }
        return builder.toString();
    }

    public static String unescape(String str, char... chars) {
        return unescape(str, "\\", chars);
    }

    public static void format(StringBuilder builder, Object... args) {
        for (int i = 0; i < args.length; i++) {
            String replacement = "{" + i + "}";
            Object arg = args[i];
            replace(builder, replacement, String.valueOf(arg));
        }
    }

    public static String format(String str, Object... args) {
        if (args.length == 0) {
            return str;
        }
        StringBuilder builder = new StringBuilder(str);
        format(builder, args);
        return builder.toString();
    }

    public static String[] splitByDelimiter(String line, char delimiter, int limit, char escape, char open, char close) {
        StringBuilder builder = new StringBuilder();
        List<String> list = new ArrayList<>();

        boolean quote = false;
        Character previous = null;
        for (int i = 0; i < line.length(); i++) {
            if (list.size() == limit) {
                break;
            }

            char c = line.charAt(i);
            builder.append(c);
            if (!quote && c == open || c == close) {
                if (previous == null || previous != escape) {
                    builder.setLength(builder.length() - 1);
                    quote = !quote;
                } else {
                    builder.setLength(builder.length() - 2);
                    builder.append(c);
                }
            } else if (!quote && c == delimiter) {
                builder.setLength(builder.length() - 1);
                list.add(builder.toString());
                builder.setLength(0);
            }

            previous = c;
        }

        if (builder.length() > 0) list.add(builder.toString());

        return list.toArray(new String[0]);
    }

    public static String[] splitByDelimiter(String line, char delimiter, int limit, char escape, char quotation) {
        return splitByDelimiter(line, delimiter, limit, escape, quotation, quotation);
    }

    public static String[] splitByDelimiter(String line, char delimiter, int limit, char escape) {
        return splitByDelimiter(line, delimiter, limit, escape, '"');
    }

    public static String[] splitByDelimiter(String line, char delimiter, int limit) {
        return splitByDelimiter(line, delimiter, limit, '\\');
    }

    public static String[] splitByDelimiter(String line, char delimiter) {
        return splitByDelimiter(line, delimiter, -2);
    }

    public static String[] splitByLength(String str, Character ignore, int length, int limit) {
        StringBuilder builder = new StringBuilder();
        List<String> list = new ArrayList<>();

        int lastStrIndex = str.length() - 1;
        boolean flag = false;
        for (int i = 0; i < str.length(); i++) {
            if (limit > 0 && list.size() == limit) {
                break;
            }
            if (flag) {
                builder.append(ignore);
                flag = false;
            }

            char c = str.charAt(i);
            if (ignore == null || c != ignore) {
                builder.append(c);
            } else {
                flag = true;
            }

            if (i == lastStrIndex || (i + 1) % length == 0) {
                list.add(builder.toString());
                builder.setLength(0);
            }
        }

        return list.toArray(new String[0]);
    }

    public static String[] splitByLength(String str, char ignore, int length) {
        return splitByLength(str, ignore, length, -1);
    }

    public static String[] splitByLength(String str, int length) {
        return splitByLength(str, null, length, -1);
    }

    public static String upperFirstCase(String str) {
        char[] chars = str.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    public static boolean isDigit(String input) {
        for (int i = 0; i < input.length(); i++){
            if (!Character.isDigit(input.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static byte parseByte(String str, byte def) {
        try {
            return Byte.parseByte(str);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static short parseShort(String str, short def) {
        try {
            return Short.parseShort(str);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static int parseInt(String str, int def) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static long parseLong(String str, long def) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static float parseFloat(String str, float def) {
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static double parseDouble(String str, double def) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static Byte parseByte(Object obj, Byte def) {
        try {
            return Byte.parseByte(String.valueOf(obj));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static Byte parseByte(Object obj) {
        return parseByte(obj, null);
    }

    public static Short parseShort(Object obj, Short def) {
        try {
            return Short.parseShort(String.valueOf(obj));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static Short parseShort(Object obj) {
        return parseShort(obj, null);
    }

    public static Integer parseInt(Object obj, Integer def) {
        try {
            return Integer.parseInt(String.valueOf(obj));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static Integer parseInt(Object obj) {
        return parseInt(obj, null);
    }

    public static Long parseLong(Object obj, Long def) {
        try {
            return Long.parseLong(String.valueOf(obj));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static Long parseLong(Object obj) {
        return parseLong(obj, null);
    }

    public static Float parseFloat(Object obj, Float def) {
        try {
            return Float.parseFloat(String.valueOf(obj));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static Float parseFloat(Object obj) {
        return parseFloat(obj, null);
    }

    public static Double parseDouble(Object obj, Double def) {
        try {
            return Double.parseDouble(String.valueOf(obj));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static Double parseDouble(Object obj) {
        return parseDouble(obj, null);
    }

    public static String inflect(long value, String var1, String var2, String var3, String var4) {
        if (value == 0) {
            return var4;
        }

        long r1;
        if (value % 10 == 0 || (value / 10) % 10 == 1 || (r1 = value % 10) > 4) {
            return var3;
        } else if (r1 != 1) {
            return var2;
        } else {
            return var1;
        }
    }

    public static String inflect(long value, String var1, String var2, String var3) {
        return inflect(value, var1, var2, var3, var3);
    }

    public static String inflect(long value, String var1, String var2) {
        return inflect(value, var1, var2, var2);
    }
}
