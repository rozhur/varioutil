package org.zhdev.varioutil.sql;

import org.zhdev.varioutil.util.SqlUtils;

public class SqliteProvider extends AbstractProvider {
    public SqliteProvider(String path) {
        super(SqlUtils.createSqliteConnection(path));
    }
}
