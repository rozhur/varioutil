package org.zhdev.varioutil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;
import org.zhdev.varioutil.bukkit.command.PreparedPluginCommand;
import org.zhdev.varioutil.config.BukkitYamlConfig;
import org.zhdev.varioutil.config.Config;
import org.zhdev.varioutil.config.ConfigSection;
import org.zhdev.varioutil.config.YamlConfig;
import org.zhdev.varioutil.language.Language;
import org.zhdev.varioutil.sql.SqlAdapter;
import org.zhdev.varioutil.util.BukkitUtils;
import org.zhdev.varioutil.util.CollectionUtils;
import org.zhdev.varioutil.util.ColorUtils;
import org.zhdev.varioutil.util.ConfigUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public abstract class BukkitPreparedPlugin extends BukkitPlugin implements Listener, PreparedPlugin {
    protected final Path dataDirectory;
    protected final Config defaultConfig = new BukkitYamlConfig();
    protected final Language language = new Language();
    protected final SqlAdapter sqlAdapter = createSqlAdapter();

    private ExecutorService singleThreadExecutor;

    public BukkitPreparedPlugin() {
        super();
        dataDirectory = getDataFolder().toPath();
    }

    protected BukkitPreparedPlugin(@NotNull JavaPluginLoader loader, @NotNull PluginDescriptionFile description, @NotNull File dataFolder, @NotNull File file) {
        super(loader, description, dataFolder, file);
        dataDirectory = getDataFolder().toPath();
    }

    protected SqlAdapter createSqlAdapter() {
        return new SqlAdapter();
    }

    protected void closeSqlAdapter() {
        sqlAdapter.close();
    }

    public Command registerPreparedCommand(CommandExecutor executor, TabCompleter completer, String permission, String label, String... aliases) {
        Command command = super.registerCommand(executor, completer, label);
        if (command == null) {
            command = new PreparedPluginCommand(label, this, executor, completer);
            command.setPermission(permission);
            command.setAliases(Arrays.asList(aliases));
            ConfigSection commandsSection = defaultConfig.getSection("commands");
            if (commandsSection != null) {
                Object rawSection = commandsSection.get(label.toLowerCase());
                if (rawSection instanceof Boolean && !((Boolean) rawSection)) {
                    return command;
                }
                if (rawSection instanceof ConfigSection) {
                    ConfigSection commandSection = (ConfigSection) rawSection;
                    command.setAliases(CollectionUtils.mapToString(commandSection.getList("aliases", command.getAliases()), ArrayList::new));
                    command.setDescription(commandSection.getString("description"));
                    command.setUsage(commandSection.getString("usage"));
                    String configPermission = commandSection.getString("permission", permission);
                    command.setPermission(configPermission == null || configPermission.isEmpty() || configPermission.equals("false") ? null : configPermission);
                    command.setPermissionMessage(commandSection.getString("permission-message"));
                }
            }
            BukkitUtils.registerCommand(getName(), label, command);
        } else {
            if (command.getPermission() == null) command.setPermission(permission);
            if (command.getAliases().isEmpty()) command.setAliases(Arrays.asList(aliases));
        }
        return command;
    }

    public Command registerPreparedCommand(CommandExecutor executor, String permission, String label, String... aliases) {
        return registerPreparedCommand(executor, this, permission, label, aliases);
    }

    @Override
    public Command registerCommand(CommandExecutor executor, TabCompleter completer, String label) {
        return registerPreparedCommand(executor, completer, null, label);
    }

    @Override
    public Path getDataDirectory() {
        return dataDirectory;
    }

    @Deprecated
    @NotNull
    @Override
    public final FileConfiguration getConfig() {
        throw new UnsupportedOperationException("You should use PreparedBukkitPlugin#getConfiguration");
    }

    public Config getDefaultConfig() {
        return defaultConfig;
    }

    public void loadDefaultConfig() {
        loadConfig(defaultConfig);
    }

    public Language getLanguage() {
        return language;
    }

    public SqlAdapter getSqlAdapter() {
        return sqlAdapter;
    }

    protected void loadPhrases() {
        ConfigSection languageSection = defaultConfig.getOrCreateSection("language");
        String locale = languageSection.getString("locale", "default");
        Config languageConfig = new YamlConfig("language/" + locale + ".yml");
        loadConfig(languageConfig, "language.yml", true);
        loadConfig(languageConfig, "language/default.yml", true);
        loadConfig(languageConfig, true);
        ConfigUtils.addPhrases(language, languageConfig, ColorUtils::translateAlternateColorCodes);
    }

    protected void establishSqlConnection() {
        Config databaseConfig = new BukkitYamlConfig("database.yml");
        loadConfig(databaseConfig);
        sqlAdapter.setProvider(ConfigUtils.createSqlConnectionProvider(databaseConfig, getDataFolder().getPath()));
    }

    protected void onLoading() {}

    protected void onEnabling() {}

    protected void onDisabling() {}

    private void load() {
        loadDefaultConfig();
        loadPhrases();
        establishSqlConnection();
    }

    @Override
    public final void onLoad() {
        load();
        onLoading();
    }

    private void enable() {
        registerEvents(this);
    }

    @Override
    public final void onEnable() {
        enable();
        onEnabling();
    }

    private void disable() {
        closeSqlAdapter();
        defaultConfig.clear();

        if (singleThreadExecutor != null) {
            singleThreadExecutor.shutdownNow();
        }

        BukkitUtils.unregisterCommandIf(command -> command instanceof PluginIdentifiableCommand && ((PluginIdentifiableCommand) command).getPlugin() == this);
    }

    @Override
    public final void onDisable() {
        try {
            onDisabling();
            disable();
        } catch (Exception e) {
            disable();
            throw e;
        }
    }

    public void runTaskSeparately(Runnable runnable) {
        if (singleThreadExecutor == null) {
            singleThreadExecutor = Executors.newSingleThreadExecutor();
        }

        singleThreadExecutor.execute(() -> {
            try {
                runnable.run();
            } catch (Throwable throwable) {
                getLogger().log(Level.SEVERE, "Unhandled exception while running separated task", throwable);
            }
        });
    }
}
