package org.zhdev.varioutil;

import org.zhdev.varioutil.config.Config;
import org.zhdev.varioutil.config.ConfigException;
import org.zhdev.varioutil.language.Language;
import org.zhdev.varioutil.sql.SqlAdapter;
import org.zhdev.varioutil.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

public interface PreparedPlugin {
    Logger getLogger();

    Config getDefaultConfig();

    Language getLanguage();

    SqlAdapter getSqlAdapter();

    Path getDataDirectory();

    default Path loadConfig(Config config, String pathname) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream stream = ResourceUtils.getResource(pathname, classLoader);
        if (stream != null) {
            config.load(stream);
        }

        Path path = config.load(getDataDirectory().toString() + '/' + pathname);
        try {
            if (Files.notExists(path) || Files.size(path) == 0) {
                if (ResourceUtils.saveResource(pathname, path, classLoader)) {
                    config.load(path);
                }
            }
        } catch (IOException e) {
            throw new ConfigException(e);
        }
        return path;
    }

    default Path loadConfig(Config config) {
        return loadConfig(config, config.getKey());
    }

    default Path saveConfig(Config config, String pathname) {
        return config.save(getDataDirectory().toString() + '/' + pathname);
    }

    default Path saveConfigIfEmpty(Config config, String pathname) {
        return config.saveIfEmpty(getDataDirectory().toString() + '/' + pathname);
    }

    default Path saveConfig(Config config) {
        return saveConfig(config, config.getKey());
    }

    default Path saveConfigIfEmpty(Config config) {
        return saveConfigIfEmpty(config, config.getKey());
    }
}
