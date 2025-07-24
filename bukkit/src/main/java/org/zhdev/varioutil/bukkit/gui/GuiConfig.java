package org.zhdev.varioutil.bukkit.gui;

import org.zhdev.varioutil.config.ConfigSection;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiConfig {
    private final GuiType type;
    private final Map<Integer, GuiIconConfig> iconMap = new HashMap<>();
    private final Map<Character, Integer> charToSlotMap = new HashMap<>();

    public GuiConfig(ConfigSection config) {
        ConfigSection itemsSection = config.getOrCreateSection("items");
        List<?> list = config.getList("shape", Collections.emptyList());
        int x = 0, maxSlot = 0;
        for (; x < list.size(); x++) {
            String l = String.valueOf(list.get(x));
            int y;
            for (y = 0; y < l.length(); y++) {
                char c = l.charAt(y);
                if (c == ' ') continue;
                ConfigSection iconSection = itemsSection.getOrCreateSection(String.valueOf(c));
                GuiIconConfig iconConfig = new GuiIconConfig(iconSection);
                int slot = x * 9 + y;
                iconMap.put(slot, iconConfig);
                charToSlotMap.put(c, slot);
            }
            if (y > maxSlot) maxSlot = y;
        }

        GuiType type;
        try {
            if (x == 1 && maxSlot < 6) {
                type = GuiType.SIZE_5;
            } else if (x == 3 && maxSlot < 4) {
                type = GuiType.SIZE_3X3;
            } else if (x > 0) {
                type = GuiType.valueOf("SIZE_" + (x * 9));
            } else {
                type = GuiType.SIZE_9;
            }
        } catch (IllegalArgumentException e) {
            type = GuiType.SIZE_9;
        }

        this.type = type;
    }

    public GuiType getType() {
        return type;
    }

    public GuiIconConfig getIconConfig(int slot) {
        return iconMap.get(slot);
    }

    public int getSlot(char c) {
        return charToSlotMap.get(c);
    }
}
