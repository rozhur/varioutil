package org.zhdev.util;

import org.zhdev.config.ConfigSection;
import org.zhdev.language.Language;
import org.zhdev.sql.*;

import java.io.File;

public class ConfigUtils {
    public static void addPhrases(Language language, ConfigSection config) {
        for (String i : config) {
            ConfigSection section = config.getSection(i);
            if (section == null) continue;
            for (String j : section) {
                language.addPhrase(i, j, section.getString(j));
            }
        }
    }

    public static SqlConnection createSqlConnection(ConfigSection config, String pathPrefix) {
        String type = config.getString("type", "none").toLowerCase();
        SqlConnection connection;
        switch (type) {
            case "h2": {
                String path = config.getString("path");
                String username = config.getString("username");
                String password = config.getString("password");
                connection = new H2SqlConnection(File.separatorChar + path, username, password);
                break;
            }
            case "mysql": {
                String address = config.getString("address", "127.0.0.1");
                pathPrefix = new File(pathPrefix).getAbsoluteFile().getName().replace(File.separator, "_").toLowerCase();
                String dbname = config.getString("dbname", System.getProperty("user.home") + '_' + pathPrefix);
                String username = config.getString("username", System.getProperty("user.home"));
                String password = config.getString("password");
                boolean ssl = config.getBoolean("ssl", false);
                connection = new MySqlConnection(address, dbname, username, password, ssl);
                break;
            }
            case "sqlite": {
                String path = config.getString("path");
                connection = new SqliteConnection(pathPrefix + File.separatorChar + path);
                break;
            }
            default: case "none": connection = SqlConnection.NOT_ESTABLISHED;
        }
        return connection;
    }
}
