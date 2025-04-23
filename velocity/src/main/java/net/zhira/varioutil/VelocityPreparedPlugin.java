package net.zhira.varioutil;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import net.zhira.varioutil.config.ConfigSection;
import net.zhira.varioutil.language.Language;
import net.zhira.varioutil.util.ColorUtils;
import org.slf4j.Logger;
import net.zhira.varioutil.config.Config;
import net.zhira.varioutil.config.VelocityTomlConfig;
import net.zhira.varioutil.sql.SqlAdapter;
import net.zhira.varioutil.util.ConfigUtils;

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
