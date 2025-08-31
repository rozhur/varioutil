package org.zhdev;

import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

public abstract class VelocityPlugin {
    protected final ProxyServer server;
    protected final Logger logger;
    protected final Path dataFolder;

    protected VelocityPlugin(ProxyServer server, Logger logger, Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataFolder = dataDirectory;
    }

    public ProxyServer getServer() {
        return server;
    }

    public Path getDataDirectory() {
        return dataFolder;
    }

    public java.util.logging.Logger getLogger() {
        return java.util.logging.Logger.getLogger(logger.getName());
    }
}
