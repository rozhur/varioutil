package org.zhdev.varioutil.bukkit.gui;

import org.zhdev.varioutil.config.ConfigSection;
import org.zhdev.varioutil.util.StringUtils;
import org.bukkit.Material;
import org.zhdev.varioutil.bukkit.LegacyMaterial;

public class GuiIconConfig {
    protected final Material type;
    protected final Byte data;
    protected final int amount;
    protected final String texture;

    public GuiIconConfig(ConfigSection config) {
        String[] rawType = config.getString("type", "AIR").toUpperCase().split(":", 2);
        Material type = LegacyMaterial.of(rawType[0]);
        Byte data = rawType.length > 1 ? StringUtils.parseByte(rawType[1]) : null;
        if (type == null) type = Material.STONE;
        int amount = config.getInteger("amount", 1);
        String texture = config.getString("texture");
        this.type = type;
        this.data = data;
        this.amount = amount;
        this.texture = texture;
    }

    public Material getType() {
        return type;
    }

    public Byte getData() {
        return data;
    }

    public int getAmount() {
        return amount;
    }

    public String getTexture() {
        return texture;
    }

    public GuiIcon.StackBuilder stackBuilder() {
        return new GuiIcon()
                .stackBuilder(type, data)
                .amount(amount)
                .texture(texture)
                .flagsAll();
    }

    public GuiIcon create() {
        return stackBuilder().build();
    }
}
