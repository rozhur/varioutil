package org.zhdev.varioutil.bukkit.gui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.zhdev.varioutil.util.BukkitUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GuiIcon {
    ItemStack stack;
    ClickHandler clickHandler;

    public ItemStack stack() {
        return stack;
    }

    public GuiIcon stack(ItemStack stack) {
        this.stack = stack;
        return this;
    }

    public StackBuilder stackBuilder(Material type) {
        return new StackBuilder(type);
    }

    public StackBuilder stackBuilder(Material type, Byte data) {
        return new StackBuilder(type, data);
    }

    public ClickHandler clickHandler() {
        return clickHandler;
    }

    public GuiIcon clickHandler(ClickHandler clickHandler) {
        this.clickHandler = clickHandler;
        return this;
    }

    public class StackBuilder {
        private final Material type;
        private final Byte data;
        private int amount = 1;
        private String displayName;
        private List<String> lore;
        private ItemFlag[] flags;
        private String texture;

        private StackBuilder(Material type) {
            this.type = type;
            this.data = null;
        }

        private StackBuilder(Material type, Byte data) {
            this.type = type;
            this.data = data;
        }

        public StackBuilder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public StackBuilder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public StackBuilder lore(List<String> lore) {
            this.lore = lore;
            return this;
        }

        public StackBuilder lore(String lore) {
            return lore(Collections.singletonList(lore));
        }

        public StackBuilder lore(String... lore) {
            return lore(Arrays.asList(lore));
        }

        public StackBuilder flags(ItemFlag... flags) {
            this.flags = flags;
            return this;
        }

        public StackBuilder flagsAll() {
            this.flags = ItemFlag.values();
            return this;
        }

        public StackBuilder texture(String texture) {
            this.texture = texture;
            return this;
        }

        public GuiIcon build() {
            ItemStack stack;
            if (data == null) stack = new ItemStack(type, amount);
            else stack = new ItemStack(type, amount, (short) 0, data);
            ItemMeta meta = stack.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(displayName);
                meta.setLore(lore);
                meta.addItemFlags(flags);
                if (texture != null && meta instanceof SkullMeta) {
                    BukkitUtils.setSkullTexture(((SkullMeta) meta), texture);
                }
                stack.setItemMeta(meta);
            }
            GuiIcon.this.stack = stack;
            return GuiIcon.this;
        }
    }
}

