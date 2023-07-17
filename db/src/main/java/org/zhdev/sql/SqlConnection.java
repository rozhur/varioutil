package org.zhdev.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlConnection extends AutoCloseable {
    SqlConnection NOT_ESTABLISHED = new SqlConnection() {
        @Override
        public PreparedStatement prepareStatement(String query) {
            throw new SqlException("Connection not established");
        }

        @Override
        public boolean isClosed() {
            return true;
        }

        @Override
        public void close() {
            throw new SqlException("Connection not established");
        }
    };
    SqlConnection CLOSED = new SqlConnection() {
        @Override
        public PreparedStatement prepareStatement(String query) {
            throw new SqlException("Connection closed");
        }

        @Override
        public boolean isClosed() {
            return true;
        }

        @Override
        public void close() {
            throw new SqlException("Connection closed");
        }
    };

    PreparedStatement prepareStatement(String query) throws SQLException;

    boolean isClosed();

    @Override
    void close();
}
