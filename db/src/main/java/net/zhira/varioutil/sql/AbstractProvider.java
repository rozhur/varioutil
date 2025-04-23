package net.zhira.varioutil.sql;

import java.sql.Connection;
import java.sql.SQLException;

abstract class AbstractProvider implements ConnectionProvider {
    private final Connection connection;

    AbstractProvider(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public boolean isClosed() {
        try {
            return connection.isClosed();
        } catch (SQLException e) {
            throw new SqlException(e);
        }
    }
}
