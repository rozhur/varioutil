package org.zhdev.varioutil.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider {
    ConnectionProvider NOT_ESTABLISHED = new NullProvider("Connection not established");
    ConnectionProvider CLOSED = new NullProvider("Connection closed");

    Connection getConnection();

    default boolean isClosed() {
        return true;
    }

    default void close() {
        try {
            getConnection().close();
        } catch (SQLException e) {
            throw new SqlException(e);
        }
    }
}