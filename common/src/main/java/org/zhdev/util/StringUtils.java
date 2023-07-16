package org.zhdev.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StringUtils {
    public static String join(int beginIndex, int lastIndex, String delimiter, String lastDelimiter, String... elements) {
        StringBuilder builder = new StringBuilder();
        for (; beginIndex < elements.length; beginIndex++) {
            if (beginIndex == lastIndex) {
                builder.append(lastDelimiter);
            } else {
                builder.append(delimiter);
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

        list.add(builder.toString());

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
}
