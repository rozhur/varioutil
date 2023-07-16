package org.zhdev.config;

import java.io.*;

public interface Config extends ConfigSection {
    String DEFAULT_KEY = "config.yml";

    String getKey();

    String[] getHeaderComments();

    String[] getEndComments();

    void setHeaderComments(String... headerComments);

    void setEndComments(String... endComments);

    void load(Reader reader);

    void load(InputStream stream) throws IOException, ConfigException;

    void load(File file) throws IOException, ConfigException;

    File load(String path) throws ConfigException;

    File load() throws ConfigException;

    void save(Writer writer);

    void save(OutputStream stream) throws IOException, ConfigException;

    void save(File file) throws IOException, ConfigException;

    File save(String path) throws ConfigException;

    File save() throws ConfigException;

    File saveIfEmpty(String path) throws ConfigException;

    File saveIfEmpty() throws ConfigException;
}
