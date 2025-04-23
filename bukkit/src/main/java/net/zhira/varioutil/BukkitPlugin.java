package net.zhira.varioutil;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public abstract class BukkitPlugin extends JavaPlugin {
    public BukkitPlugin() {
        super();
    }

    protected BukkitPlugin(@NotNull JavaPluginLoader loader, @NotNull PluginDescriptionFile description, @NotNull File dataFolder, @NotNull File file) {
        super(loader, description, dataFolder, file);
    }

    public Command registerCommand(CommandExecutor executor, TabCompleter completer, String label) {
        PluginCommand command = Bukkit.getPluginCommand(label);
        if (command != null) {
            command.setExecutor(executor);
            command.setTabCompleter(completer);
        }
        return command;
    }

    public Command registerCommand(CommandExecutor executor, String label) {
        return registerCommand(executor, executor instanceof TabCompleter ? (TabCompleter) executor : this, label);
    }

    public void registerEvents(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public void cancelTask(int taskId) {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    public void cancelTasks() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    @NotNull
    public BukkitTask runTask(@NotNull Runnable task) throws IllegalArgumentException {
        return Bukkit.getScheduler().runTask(this, task);
    }

    @NotNull
    public BukkitTask runTaskAsynchronously(@NotNull Runnable task) throws IllegalArgumentException {
        return Bukkit.getScheduler().runTaskAsynchronously(this, task);
    }

    @NotNull
    public BukkitTask runTaskLater(@NotNull Runnable task, long delay) throws IllegalArgumentException {
        return Bukkit.getScheduler().runTaskLater(this, task, delay);
    }

    @NotNull
    public BukkitTask runTaskLaterAsynchronously(@NotNull Runnable task, long delay) throws IllegalArgumentException {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(this, task, delay);
    }

    @NotNull
    public BukkitTask runTaskTimer(@NotNull Runnable task, long delay, long period) throws IllegalArgumentException {
        return Bukkit.getScheduler().runTaskTimer(this, task, delay, period);
    }

    @NotNull
    public BukkitTask runTaskTimerAsynchronously(@NotNull Runnable task, long delay, long period) throws IllegalArgumentException {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(this, task, delay, period);
    }
}
