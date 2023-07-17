package org.zhdev;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.zhdev.config.BukkitConfig;
import org.zhdev.config.BukkitYamlConfig;
import org.zhdev.language.Language;
import org.zhdev.sql.SqlAdapter;
import org.zhdev.util.ConfigUtils;

public abstract class PreparedBukkitPlugin extends BukkitPlugin implements Listener {
    protected final BukkitConfig configuration = new BukkitYamlConfig();
    protected final Language language = new Language();
    protected final SqlAdapter sqlAdapter = new SqlAdapter();

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

    protected void onLoading() {}

    protected void onEnabling() {}

    protected void onDisabling() {}

    @Override
    public final void onLoad() {
        configuration.load(this);

        String locale = configuration.getString("locale", "default");
        BukkitConfig languageConfig = new BukkitYamlConfig("language/" + locale + ".yml");
        languageConfig.load(this);
        ConfigUtils.addPhrases(language, languageConfig);

        BukkitConfig databaseConfig = new BukkitYamlConfig("database.yml");
        databaseConfig.load(this);
        sqlAdapter.setConnection(ConfigUtils.createSqlConnection(databaseConfig, getDataFolder().getPath()));

        onLoading();
    }

    @Override
    public final void onEnable() {
        registerEvents(this);
        onEnabling();
    }

    @Override
    public final void onDisable() {
        onDisabling();
        sqlAdapter.close();
        configuration.clear();
    }
}
