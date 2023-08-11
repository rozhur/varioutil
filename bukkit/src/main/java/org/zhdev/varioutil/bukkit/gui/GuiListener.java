package org.zhdev.varioutil.bukkit.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class GuiListener implements Listener {
    private final Plugin plugin;

    public GuiListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        if (!(event.getInventory().getHolder() instanceof GuiHolder)) {
            return;
        }

        switch (event.getAction()) {
            case CLONE_STACK: case COLLECT_TO_CURSOR: case MOVE_TO_OTHER_INVENTORY: event.setCancelled(true);
                break;
        }

        int slot = event.getRawSlot();
        if (slot > event.getInventory().getSize() - 1) {
            return;
        }

        GuiHolder holder = (GuiHolder) event.getInventory().getHolder();
        GuiIcon icon = holder.gui.getIcon(slot);
        if (icon == null || icon.clickHandler == null) {
            event.setCancelled(true);
            return;
        }

        Boolean result = icon.clickHandler.apply(event);
        event.setCancelled(result == null || result);
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin() == plugin) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Inventory inventory = player.getOpenInventory().getTopInventory();
                if (inventory.getHolder() instanceof GuiHolder) player.closeInventory();
            }
        }
    }
}
