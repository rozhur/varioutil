package net.zhira.varioutil.sql;

import net.zhira.varioutil.util.SqlUtils;

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
