package org.zhdev.sql;

import org.zhdev.util.SqlUtils;

public final class MySqlConnection extends AbstractSqlConnection {
    public MySqlConnection(String address, String dbname, String username, String password, boolean ssl) {
        super(SqlUtils.createMySqlConnection(address, dbname, username, password, ssl));
    }
}
