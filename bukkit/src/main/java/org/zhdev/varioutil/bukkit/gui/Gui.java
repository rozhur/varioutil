package org.zhdev.varioutil.bukkit.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Gui {
    final GuiHolder holder = new GuiHolder(this);

    private final Map<Integer, GuiIcon> iconMap = new HashMap<>();

    private String title;
    private GuiType type = GuiType.SIZE_27;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public GuiType getType() {
        return type;
    }

    public void setType(GuiType type) {
        this.type = type;
    }

    public GuiIcon getIcon(int slot) {
        return iconMap.get(slot);
    }

    public GuiIcon removeIcon(int slot) {
        GuiIcon existsIcon = iconMap.remove(slot);
        holder.inventory.setItem(slot, null);
        return existsIcon;
    }

    public void updateIconIfChanged(int slot, GuiIcon icon) {
        ItemStack exists = holder.inventory.getItem(slot);
        if (exists != null && exists.getType() != Material.AIR && exists.equals(icon.stack)) {
            return;
        }
        iconMap.put(slot, icon);
        holder.inventory.setItem(slot, icon.stack);
    }

    public void updateIcon(int slot, GuiIcon icon) {
        iconMap.put(slot, icon);
        holder.inventory.setItem(slot, icon.stack);
    }

    public void createInventory() {
        holder.inventory = type.createInventory(holder, title);
    }

    public void updateChangedIcons() {
        for (int i = 0; i < holder.inventory.getSize(); i++) {
            GuiIcon icon = iconMap.get(i);
            ItemStack exists = holder.inventory.getItem(i);
            if (icon == null) {
                if (exists != null) holder.inventory.setItem(i, null);
                continue;
            }
            if (exists != null && exists.getType() != Material.AIR && exists.equals(icon.stack)) {
                continue;
            }
            holder.inventory.setItem(i, icon.stack);
        }
    }

    public void update() {
        for (int i = 0; i < holder.inventory.getSize(); i++) {
            GuiIcon icon = iconMap.get(i);
            if (icon == null) {
                holder.inventory.setItem(i, null);
            } else {
                holder.inventory.setItem(i, icon.stack);
            }
        }
    }

    public void open(Player viewer) {
        Inventory inventory = viewer.getOpenInventory().getTopInventory();
        if (inventory.getHolder() != holder) {
            viewer.openInventory(holder.inventory);
        }
    }
}
