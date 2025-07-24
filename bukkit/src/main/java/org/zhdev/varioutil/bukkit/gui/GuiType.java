package org.zhdev.varioutil.bukkit.gui;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public enum GuiType {
    SIZE_3X3(9) {
        @Override
        public Inventory createInventory(InventoryHolder holder, String title) {
            return Bukkit.createInventory(holder, InventoryType.WORKBENCH, title);
        }
    },
    SIZE_5(5) {
        @Override
        public Inventory createInventory(InventoryHolder holder, String title) {
            return Bukkit.createInventory(holder, InventoryType.HOPPER, title);
        }
    },
    SIZE_9(9),
    SIZE_18(18),
    SIZE_27(27),
    SIZE_36(36),
    SIZE_45(45),
    SIZE_54(54);

    private final int size;

    GuiType(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public Inventory createInventory(InventoryHolder holder, String title) {
        return Bukkit.createInventory(holder, size, title);
    }
}
