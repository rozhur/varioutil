package net.zhira.varioutil.sql;

import net.zhira.varioutil.util.SqlUtils;

public class MysqlProvider extends AbstractProvider {
    public MysqlProvider(String address, String dbname, String username, String password, boolean ssl) {
        super(SqlUtils.createMysqlConnection(address, dbname, username, password, ssl));
    }
}
