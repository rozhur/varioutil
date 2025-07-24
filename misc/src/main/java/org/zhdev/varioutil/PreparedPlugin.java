package org.zhdev.varioutil;

import org.zhdev.varioutil.config.Config;
import org.zhdev.varioutil.config.ConfigException;
import org.zhdev.varioutil.language.Language;
import org.zhdev.varioutil.util.ResourceUtils;
import org.zhdev.varioutil.sql.SqlAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public interface PreparedPlugin {
    Logger getLogger();

    Config getDefaultConfig();

    Language getLanguage();

    SqlAdapter getSqlAdapter();

    Path getDataDirectory();

    default Path loadConfig(Config config, String pathname, boolean resourcePreload) {
        ClassLoader classLoader = getClass().getClassLoader();
        Path path = Paths.get(getDataDirectory().toString() + '/' + pathname);
        try (InputStream stream = ResourceUtils.getResource(pathname, classLoader)) {
            if (stream != null) {
                if (resourcePreload) {
                    config.load(stream);
                }

                if (Files.notExists(path) || Files.size(path) == 0) {
                    ResourceUtils.saveResource(stream, path);
                }
            }

            if (Files.exists(path)) config.load(path);
        } catch (IOException e) {
            throw new ConfigException(e);
        }
        return path;
    }

    default Path loadConfig(Config config, String pathname) {
        return loadConfig(config, pathname, false);
    }

    default Path loadConfig(Config config, boolean resourcePreload) {
        return loadConfig(config, config.getKey(), resourcePreload);
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
