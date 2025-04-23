package net.zhira.varioutil.bukkit.command;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class PreparedPluginCommand extends AbstractPluginCommand {
    private final CommandExecutor executor;
    private final TabCompleter completer;

    public PreparedPluginCommand(@NotNull String name, Plugin plugin, CommandExecutor executor, TabCompleter completer) {
        super(name, plugin);
        this.executor = executor;
        this.completer = completer;
    }

    @Override
    protected boolean onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        return executor.onCommand(sender, this, label, args);
    }

    @Override
    protected List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        return completer == null ? null : completer.onTabComplete(sender, this, label, args);
    }
}
