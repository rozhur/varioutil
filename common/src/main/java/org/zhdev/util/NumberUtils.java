package org.zhdev.util;

public class NumberUtils {
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

    public static double parseFloat(String str, float def) {
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
}
