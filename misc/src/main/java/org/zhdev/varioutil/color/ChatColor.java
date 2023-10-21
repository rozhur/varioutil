package org.zhdev.varioutil.color;

public interface ChatColor {
    char CHAR = '§';

    NamedChatColor BLACK = new NamedChatColor(0x000000, "black", '0');
    NamedChatColor DARK_BLUE = new NamedChatColor(0x0000aa, "dark_blue", '1');
    NamedChatColor DARK_GREEN = new NamedChatColor(0x00aa00, "dark_green", '2');
    NamedChatColor DARK_AQUA = new NamedChatColor(0x00aaaa, "dark_aqua", '3');
    NamedChatColor DARK_RED = new NamedChatColor(0xaa0000, "dark_red", '4');
    NamedChatColor DARK_PURPLE = new NamedChatColor(0xaa00aa, "dark_purple", '5');
    NamedChatColor GOLD = new NamedChatColor(0xffaa00, "gold", '6');
    NamedChatColor GRAY = new NamedChatColor(0xaaaaaa, "gray", '7');
    NamedChatColor DARK_GRAY = new NamedChatColor(0x555555, "dark_gray", '8');
    NamedChatColor BLUE = new NamedChatColor(0x5555ff, "blue", '9');
    NamedChatColor GREEN = new NamedChatColor(0x55ff55, "green", 'a');
    NamedChatColor AQUA = new NamedChatColor(0x55ffff, "aqua", 'b');
    NamedChatColor RED = new NamedChatColor(0xff5555, "red", 'c');
    NamedChatColor LIGHT_PURPLE = new NamedChatColor(0xff55ff, "light_purple", 'd');
    NamedChatColor YELLOW = new NamedChatColor(0xffff55, "yellow", 'e');
    NamedChatColor WHITE = new NamedChatColor(0xffffff, "white", 'f');

    NamedChatColor OBFUSCATED = new NamedChatColor("obfuscated", 'k');
    NamedChatColor BOLD = new NamedChatColor("bold", 'l');
    NamedChatColor STRIKETHROUGH = new NamedChatColor("strikethrough", 'm');
    NamedChatColor UNDERLINED = new NamedChatColor("underlined", 'n');
    NamedChatColor ITALIC = new NamedChatColor("italic", 'o');
    NamedChatColor RESET = new NamedChatColor("reset", 'r');

    int getValue();

    String getName();

    static ChatColor valueOf(int value) {
        return ChatColorCache.BY_VALUE.computeIfAbsent(value, HexChatColor::new);
    }

    static ChatColor valueOf(char code) {
        return ChatColorCache.BY_CODE.get(code);
    }

    static ChatColor valueOf(String name) {
        return ChatColorCache.BY_NAME.get(name);
    }

    static ChatColor[] values() {
        return ChatColorCache.BY_NAME.values().toArray(new ChatColor[0]);
    }
}
