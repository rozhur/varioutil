package org.zhdev;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.zhdev.config.ConfigSection;
import org.jetbrains.annotations.NotNull;
import org.zhdev.config.BukkitConfig;
import org.zhdev.config.BukkitYamlConfig;
import org.zhdev.language.Language;
import org.zhdev.sql.SqlAdapter;
import org.zhdev.util.ColorUtils;
import org.zhdev.util.ConfigUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public abstract class PreparedBukkitPlugin extends BukkitPlugin implements Listener {
    protected final BukkitConfig configuration = new BukkitYamlConfig();
    protected final Language language = new Language();
    protected final SqlAdapter sqlAdapter = new SqlAdapter();

    private ExecutorService singleThreadExecutor;

    @Deprecated
    @NotNull
    @Override
    public FileConfiguration getConfig() {
        throw new UnsupportedOperationException("You should use PreparedBukkitPlugin#getConfiguration");
    }

    public BukkitConfig getConfiguration() {
        return configuration;
    }

    public Language getLanguage() {
        return language;
    }

    public SqlAdapter getSqlAdapter() {
        return sqlAdapter;
    }

    protected void onLoading() {}

    protected void onEnabling() {}

    protected void onDisabling() {}

    private void load() {
        configuration.load(this);

        ConfigSection languageSection = configuration.getOrCreateSection("language");
        String locale = languageSection.getString("locale", "default");
        BukkitConfig languageConfig = new BukkitYamlConfig("language/" + locale + ".yml");
        languageConfig.load(this);
        String colorCode = languageSection.getString("color-code-char", "&");
        if (colorCode.isEmpty()) colorCode = "&";
        char colorCodeChar = colorCode.charAt(0);
        ConfigUtils.addPhrases(language, languageConfig, s -> ColorUtils.translateAlternateColorCodes(s, colorCodeChar));

        BukkitConfig databaseConfig = new BukkitYamlConfig("database.yml");
        databaseConfig.load(this);
        sqlAdapter.setConnection(ConfigUtils.createSqlConnection(databaseConfig, getDataFolder().getPath()));
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
        sqlAdapter.close();
        configuration.clear();

        if (singleThreadExecutor != null) {
            singleThreadExecutor.shutdownNow();
        }
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
