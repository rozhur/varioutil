package net.zhira.varioutil.sql;

import net.zhira.varioutil.util.SqlUtils;

import java.sql.Connection;

public class H2Provider extends AbstractProvider {
    private final String path;
    private final String username;
    private final String password;

    public H2Provider(String path, String username, String password) {
        this.path = path;
        this.username = username;
        this.password = password;
    }

    @Override
    Connection establishConnection() {
        return SqlUtils.createH2Connection(path, username, password);
    }
}
