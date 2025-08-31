package org.zhdev.bukkit.command;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class AbstractPluginCommand extends Command implements PluginIdentifiableCommand {
    private final Plugin plugin;

    protected AbstractPluginCommand(@NotNull String name, Plugin plugin) {
        super(name);
        this.plugin = plugin;
    }

    protected abstract boolean onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args);

    protected abstract List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args);

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!plugin.isEnabled()) {
            throw new CommandException("Cannot execute command '" + label + "' in plugin " + plugin.getDescription().getFullName() + " - plugin is disabled.");
        }

        if (!testPermission(sender)) {
            return true;
        }

        try {
            return onExecute(sender, label, args);
        } catch (Throwable ex) {
            throw new CommandException("Unhandled exception executing command '" + label + "' in plugin " + plugin.getDescription().getFullName(), ex);
        }
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) throws IllegalArgumentException {
        List<String> completions;
        try {
            completions = onTabComplete(sender, label, args);
        } catch (Throwable ex) {
            StringBuilder message = new StringBuilder();
            message.append("Unhandled exception during tab completion for command '/").append(label).append(' ');
            for (String arg : args) {
                message.append(arg).append(' ');
            }
            message.deleteCharAt(message.length() - 1).append("' in plugin ").append(plugin.getDescription().getFullName());
            throw new CommandException(message.toString(), ex);
        }

        if (completions == null) {
            return super.tabComplete(sender, label, args);
        }
        return completions;
    }

    @NotNull
    @Override
    public Plugin getPlugin() {
        return plugin;
    }
}
