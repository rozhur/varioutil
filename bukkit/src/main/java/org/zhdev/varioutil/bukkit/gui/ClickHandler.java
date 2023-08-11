package org.zhdev.varioutil.bukkit.gui;

import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Function;

@FunctionalInterface
public interface ClickHandler extends Function<InventoryClickEvent, Boolean> { }
