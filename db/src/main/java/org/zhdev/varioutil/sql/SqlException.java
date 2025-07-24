package org.zhdev.varioutil.sql;

public class SqlException extends RuntimeException {
    public SqlException() {
    }

    public SqlException(Throwable cause) {
        super(cause);
    }

    public SqlException(String s) {
        super(s);
    }

    public SqlException(String message, Throwable cause) {
        super(message, cause);
    }
}
