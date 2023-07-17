package org.zhdev.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

abstract class AbstractSqlConnection implements SqlConnection {
    private final Connection connection;

    AbstractSqlConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public PreparedStatement prepareStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    @Override
    public boolean isClosed() {
        try {
            return connection.isClosed();
        } catch (SQLException e) {
            throw new SqlException(e);
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new SqlException(e);
        }
    }
}
