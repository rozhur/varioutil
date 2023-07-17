package org.zhdev.sql;

import org.zhdev.util.SqlUtils;

public class SqliteConnection extends AbstractSqlConnection {
    public SqliteConnection(String path) {
        super(SqlUtils.createSqliteConnection(path));
    }
}
