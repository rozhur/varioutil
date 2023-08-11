package org.zhdev.varioutil.config;

public class ConfigException extends RuntimeException {
    public ConfigException() {
    }

    public ConfigException(Throwable cause) {
        super(cause);
    }

    public ConfigException(String s) {
        super(s);
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
