package org.zhdev;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import org.zhdev.config.ConfigSection;
import org.zhdev.language.Language;
import org.zhdev.util.ColorUtils;
import org.slf4j.Logger;
import org.zhdev.config.Config;
import org.zhdev.config.VelocityTomlConfig;
import org.zhdev.sql.SqlAdapter;
import org.zhdev.util.ConfigUtils;

import java.nio.file.Path;

public abstract class VelocityPreparedPlugin extends VelocityPlugin implements PreparedPlugin {
    protected final Config defaultConfig = new VelocityTomlConfig();
    protected final Language language = new Language();
    protected final SqlAdapter sqlAdapter = createSqlAdapter();

    protected VelocityPreparedPlugin(ProxyServer server, Logger logger, Path dataFolder) {
        super(server, logger, dataFolder);
    }

    protected SqlAdapter createSqlAdapter() {
        return new SqlAdapter();
    }

    protected void closeSqlAdapter() {
        sqlAdapter.close();
    }

    protected void loadPhrases() {
        ConfigSection languageSection = defaultConfig.getOrCreateSection("language");
        String locale = languageSection.getString("locale", "default");
        Config languageConfig = new VelocityTomlConfig("language/" + locale + ".toml");
        loadConfig(languageConfig, "language.toml", true);
        loadConfig(languageConfig, "language/default.toml", true);
        loadConfig(languageConfig, true);
        ConfigUtils.addPhrases(language, languageConfig, ColorUtils::translateAlternateColorCodes);
    }

    protected void establishSqlConnection() {
        Config databaseConfig = new VelocityTomlConfig("database.toml");
        loadConfig(databaseConfig);
        sqlAdapter.setProvider(ConfigUtils.createSqlConnectionProvider(databaseConfig, getDataDirectory().toString()));
    }

    @Override
    public Config getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public Language getLanguage() {
        return language;
    }

    @Override
    public SqlAdapter getSqlAdapter() {
        return sqlAdapter;
    }

    protected void onPreEnable() {}

    protected void onPostEnable() {}

    protected void onPreDisable() {}

    protected void onPostDisable() {}

    @Subscribe
    private void onProxyInitialization(ProxyInitializeEvent event) {
        onPreEnable();
        loadConfig(defaultConfig);
        loadPhrases();
        establishSqlConnection();
        onPostEnable();
    }

    @Subscribe
    private void onProxyInitialization(ProxyShutdownEvent event) {
        onPreDisable();
        onPostDisable();
        closeSqlAdapter();
    }
}
