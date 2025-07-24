package org.zhdev.varioutil.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {
    public static final char COLOR_CHAR = '§';

    private static final Map<String, Pattern> COMPILED_PATTERNS = new HashMap<>();

    private static Pattern getPattern(String regexp) {
        return COMPILED_PATTERNS.computeIfAbsent(regexp, s -> Pattern.compile(regexp));
    }

    public static void translateAlternateColorCodes(StringBuilder builder, char replaceChar) {
        Matcher m = getPattern(Pattern.quote(String.valueOf(replaceChar)) + "(#[0-9a-fA-F]{6})").matcher(builder);
        int start = 0;
        while (m.find(start)) {
            String hex = m.group(1);
            StringBuilder replaceBuilder = new StringBuilder().append(COLOR_CHAR).append('x');
            for (int i = 0; i < hex.length(); i++) {
                char c = hex.charAt(i);
                replaceBuilder.append(COLOR_CHAR).append(c);
            }
            builder.replace(m.start(), m.end(), replaceBuilder.toString());
            start = m.start() + replaceBuilder.length();
        }

        String chars = "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx";
        for (int i = 0; i < builder.length() - 1; ++i) {
            if (builder.charAt(i) == replaceChar && chars.indexOf(builder.charAt(i + 1)) > -1) {
                builder.setCharAt(i, COLOR_CHAR);
                builder.setCharAt(i + 1, Character.toLowerCase(builder.charAt(i + 1)));
            }
        }
    }

    public static String translateAlternateColorCodes(String str, char replaceChar) {
        StringBuilder builder = new StringBuilder(str);
        translateAlternateColorCodes(builder, replaceChar);
        return builder.toString();
    }

    public static void translateAlternateColorCodes(StringBuilder str) {
        translateAlternateColorCodes(str, '&');
    }

    public static String translateAlternateColorCodes(String str) {
        StringBuilder builder = new StringBuilder(str);
        translateAlternateColorCodes(builder, '&');
        return builder.toString();
    }
}
