package org.zhdev.varioutil;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import org.zhdev.varioutil.config.Config;
import org.zhdev.varioutil.config.ConfigSection;
import org.zhdev.varioutil.config.VelocityTomlConfig;
import org.zhdev.varioutil.language.Language;
import org.zhdev.varioutil.sql.SqlAdapter;
import org.zhdev.varioutil.util.ColorUtils;
import org.zhdev.varioutil.util.ConfigUtils;

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

    protected void onEnabling() {}

    protected void onDisabling() {}

    @Subscribe
    private void onProxyInitialization(ProxyInitializeEvent event) {
        loadConfig(defaultConfig);
        loadPhrases();
        establishSqlConnection();
        onEnabling();
    }

    @Subscribe
    private void onProxyInitialization(ProxyShutdownEvent event) {
        onDisabling();
    }
}
