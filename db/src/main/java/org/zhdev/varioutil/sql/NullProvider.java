package org.zhdev.varioutil.sql;

import java.sql.Connection;

class NullProvider implements ConnectionProvider {
    private final String message;

    NullProvider(String message) {
        this.message = message;
    }

    @Override
    public Connection getConnection() {
        throw new SqlException(message);
    }
}
