package org.zhdev.varioutil.sql;

import org.zhdev.varioutil.util.SqlUtils;

import java.sql.Connection;

public class SqliteProvider extends AbstractProvider {
    private final String path;

    public SqliteProvider(String path) {
        this.path = path;
    }

    @Override
    Connection establishConnection() {
        return SqlUtils.createSqliteConnection(path);
    }
}
