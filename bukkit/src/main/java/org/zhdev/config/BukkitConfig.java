package org.zhdev.config;

import org.bukkit.plugin.Plugin;

import java.io.File;

public interface BukkitConfig extends Config {
    File load(Plugin plugin, String path) throws ConfigException;

    File load(Plugin plugin) throws ConfigException;

    File saveIfEmpty(Plugin plugin, String path) throws ConfigException;

    File saveIfEmpty(Plugin plugin) throws ConfigException;
}
