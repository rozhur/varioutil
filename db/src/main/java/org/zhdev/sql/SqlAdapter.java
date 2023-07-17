package org.zhdev.sql;

import org.zhdev.util.CheckedFunction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public final class SqlAdapter implements AutoCloseable {
    private SqlConnection connection = SqlConnection.NOT_ESTABLISHED;

    public SqlConnection getConnection() {
        return connection;
    }

    public void setConnection(SqlConnection connection) {
        Objects.requireNonNull(connection, "connection");
        this.connection = connection;
    }

    public <T> T prepareStatement(CheckedFunction<PreparedStatement, T, SQLException> function, String query, Object... args) throws SqlException {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0, j = 1; i < args.length; i++, j++) {
                statement.setObject(j, args[i]);
            }
            return function.apply(statement);
        } catch (SQLException e) {
            throw new SqlException(e);
        }
    }

    public <T> T executeQuery(CheckedFunction<ResultSet, T, SQLException> function, String query, Object... args) throws SqlException {
        return prepareStatement(statement -> {
            try (ResultSet set = statement.executeQuery()) {
                return function.apply(set);
            }
        }, query, args);
    }

    public boolean isClosed() {
        return connection.isClosed();
    }

    public void close() {
        if (connection.isClosed()) {
            connection.close();
        }
        connection = SqlConnection.CLOSED;
    }
}
