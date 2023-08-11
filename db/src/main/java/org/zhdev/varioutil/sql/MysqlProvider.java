package org.zhdev.varioutil.sql;

import org.zhdev.varioutil.util.SqlUtils;

public class MysqlProvider extends AbstractProvider {
    public MysqlProvider(String address, String dbname, String username, String password, boolean ssl) {
        super(SqlUtils.createMysqlConnection(address, dbname, username, password, ssl));
    }
}
