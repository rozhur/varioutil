package org.zhdev.varioutil.util;

import org.zhdev.varioutil.config.ConfigSection;
import org.zhdev.varioutil.language.Language;
import org.zhdev.varioutil.sql.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;

public class ConfigUtils {
    public static void addPhrases(Language language, ConfigSection config, Function<String, String> function) {
        for (String i : config) {
            ConfigSection section = config.getSection(i);
            if (section == null) continue;
            for (String j : section) {
                String phrase = section.getString(j);
                if (phrase == null) {
                    phrase = String.join("\n", CollectionUtils.mapToString(section.getList(j, Collections.emptyList()), ArrayList::new));
                }
                language.addPhrase(i, j, function.apply(phrase));
            }
        }
    }

    public static void addPhrases(Language language, ConfigSection config) {
        addPhrases(language, config, s -> s);
    }

    public static ConnectionProvider createSqlConnectionProvider(ConfigSection config, String pathPrefix) {
        String type = config.getString("type", "none");
        ConnectionProvider provider;
        switch (type.toLowerCase()) {
            case "sqlite": {
                String path = config.getString("path", "storage.db");
                provider = new SqliteProvider(pathPrefix + File.separatorChar + path);
                break;
            }
            case "h2": {
                String path = config.getString("path", "storage.h2");
                String username = config.getString("username");
                String password = config.getString("password");
                provider = new H2Provider(pathPrefix + File.separatorChar + path, username, password);
                break;
            }
            case "mysql": {
                String address = config.getString("address", "127.0.0.1");
                String dbname = config.getString("dbname", System.getProperty("user.home"));
                String username = config.getString("username", System.getProperty("user.home"));
                String password = config.getString("password");
                boolean ssl = config.getBoolean("ssl", false);
                provider = new MysqlProvider(address, dbname, username, password, ssl);
                break;
            }
            case "none": {
                provider = ConnectionProvider.NOT_ESTABLISHED;
                break;
            }
            default: {
                throw new SqlException("Unknown database driver: " + type);
            }
        }
        return provider;
    }
}
