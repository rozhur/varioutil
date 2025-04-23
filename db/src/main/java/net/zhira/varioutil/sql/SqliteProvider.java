package net.zhira.varioutil.sql;

import net.zhira.varioutil.util.SqlUtils;

public class SqliteProvider extends AbstractProvider {
    public SqliteProvider(String path) {
        super(SqlUtils.createSqliteConnection(path));
    }
}
