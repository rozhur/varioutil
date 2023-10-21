package org.zhdev.varioutil.color;

public class HexChatColor implements ChatColor {
    final int value;
    final String name;

    final String string;

    HexChatColor(int value, String name, String string) {
        this.value = value;
        this.name = name;
        this.string = string;
    }

    private HexChatColor(int value, String name) {
        this(value, name, hexToCode(name));
    }

    HexChatColor(int value) {
        this(value, "#" + Integer.toHexString(value));
    }

    public int getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return string;
    }

    public static String hexToCode(String hexValue, char colorChar) {
        StringBuilder builder = new StringBuilder().append(colorChar).append('x');
        for (int i = 0; i < hexValue.length(); i++) {
            builder.append(colorChar).append(hexValue.charAt(i));
        }
        return builder.toString();
    }

    public static String hexToCode(String hexValue) {
        return hexToCode(hexValue, CHAR);
    }
}
