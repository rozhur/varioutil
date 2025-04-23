package net.zhira.varioutil.bukkit.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class GuiHolder implements InventoryHolder {
    private static final Inventory FALLBACK_INVENTORY = Bukkit.createInventory(null, 9, "");

    final Gui gui;
    Inventory inventory = FALLBACK_INVENTORY;

    GuiHolder(Gui gui) {
        this.gui = gui;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
