package net.zhira.varioutil.sql;

import java.sql.Connection;
import java.sql.SQLException;

abstract class AbstractProvider implements ConnectionProvider {
    private Connection connection;

    abstract Connection establishConnection();

    @Override
    public Connection getConnection() {
        try {
            if (connection == null || !connection.isValid(1)) {
                connection = establishConnection();
            }
        } catch (SQLException e) {
            throw new SqlException(e);
        }
        return connection;
    }

    @Override
    public boolean isClosed() {
        try {
            return connection == null || connection.isClosed();
        } catch (SQLException e) {
            throw new SqlException(e);
        }
    }
}
