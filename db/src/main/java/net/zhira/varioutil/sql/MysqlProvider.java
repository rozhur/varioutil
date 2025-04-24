package net.zhira.varioutil.sql;

import net.zhira.varioutil.util.SqlUtils;

import java.sql.Connection;

public class MysqlProvider extends AbstractProvider {
    private final String address;
    private final String dbname;
    private final String username;
    private final String password;
    private final boolean ssl;

    public MysqlProvider(String address, String dbname, String username, String password, boolean ssl) {
        this.address = address;
        this.dbname = dbname;
        this.username = username;
        this.password = password;
        this.ssl = ssl;
    }

    Connection establishConnection() {
        return SqlUtils.createMysqlConnection(address, dbname, username, password, ssl);
    }
}
