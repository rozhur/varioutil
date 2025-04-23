package net.zhira.varioutil.bukkit.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

public class GuiListener implements Listener {
    private final Plugin plugin;

    public GuiListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof GuiHolder) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onClick(InventoryClickEvent event) {
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
    private void onOpen(InventoryOpenEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof GuiHolder) {
            ((GuiHolder) holder).gui.getOpenEvent().accept(event);
        }
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof GuiHolder) {
            ((GuiHolder) holder).gui.getCloseEvent().accept(event);
        }
    }

    @EventHandler
    private void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin() == plugin) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Inventory inventory = player.getOpenInventory().getTopInventory();
                if (inventory.getHolder() instanceof GuiHolder) player.closeInventory();
            }
        }
    }
}
