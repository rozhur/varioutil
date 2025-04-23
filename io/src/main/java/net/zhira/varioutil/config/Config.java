package net.zhira.varioutil.config;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface Config extends ConfigSection {
    String getKey();

    void load(Reader reader);

    default void load(InputStream stream) {
        InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        load(reader);
        try {
            reader.close();
        } catch (IOException e) {
            throw new ConfigException(e);
        }
    }

    default void load(Path path) throws IOException, ConfigException {
        try (InputStream stream = Files.newInputStream(path)) {
            load(stream);
        }
    }

    default void load(File file) throws IOException, ConfigException {
        load(file.toPath());
    }

    default Path load(String pathname) throws ConfigException {
        Path path = Paths.get(pathname);
        try {
            if (Files.exists(path)) load(path);
        } catch (IOException e) {
            throw new ConfigException(e);
        }
        return path;
    }

    Path load() throws ConfigException;

    void save(Writer writer);

    default void save(OutputStream stream) {
        try (OutputStreamWriter writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8)) {
            save(writer);
            writer.flush();
        } catch (IOException e) {
            throw new ConfigException(e);
        }
    }

    default void save(Path path) throws IOException {
        try (OutputStream stream = Files.newOutputStream(path)) {
            save(stream);
        }
    }

    default void save(File file) throws IOException {
        save(file.toPath());
    }

    default Path save(String pathname) throws ConfigException {
        Path path = Paths.get(pathname);
        try {
            if (Files.notExists(path)) {
                Path parent = path.getParent();
                if (parent != null) Files.createDirectories(parent);
                Files.createFile(path);
            }
            save(path);
        } catch (IOException e) {
            throw new ConfigException(e);
        }
        return path;
    }

    Path save() throws ConfigException;

    default Path saveIfEmpty(String pathname) throws ConfigException {
        Path path = Paths.get(pathname);
        try {
            if (Files.notExists(path) || Files.size(path) == 0) {
                Path parent = path.getParent();
                if (parent != null) Files.createDirectories(parent);
                Files.createFile(path);
                save(path);
            }
        } catch (IOException e) {
            throw new ConfigException(e);
        }
        return path;
    }

    Path saveIfEmpty() throws ConfigException;
}
