package org.zhdev.varioutil.color;

public final class NamedChatColor extends HexChatColor {
    private final char code;

    NamedChatColor(int value, String name, char code) {
        super(value, name, String.valueOf(CHAR) + code);
        this.code = code;

        if (value != -1) ChatColorCache.BY_VALUE.put(value, this);
        ChatColorCache.BY_CODE.put(code, this);
        ChatColorCache.BY_NAME.put(name, this);
    }

    NamedChatColor(String name, char code) {
        this(-1, name, code);
    }

    public char getCode() {
        return code;
    }
}
