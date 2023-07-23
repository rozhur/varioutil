package org.zhdev.util;

import org.zhdev.sql.SqlException;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class SqlUtils {
    public static final char[] ESCAPE_CHARS = {'%', '_', '[', ']', '^'};

    public static Connection createMySqlConnection(String address, String dbname, String username, String password, boolean ssl) throws SqlException {
        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                Class.forName("com.mysql.jdbc.Driver");
            }
            if (!address.contains(":")) {
                address = address + ":3306";
            }
            return DriverManager.getConnection("jdbc:mysql://" + address + '/' + dbname + "?useSSL=" + ssl, username, password);
        } catch (SQLException e) {
            throw new SqlException(e);
        } catch (ClassNotFoundException e) {
            throw new SqlException("No suitable driver");
        }
    }

    public static Connection createH2Connection(String path, String username, String password) throws SqlException {
        try {
            Class.forName("org.h2.Driver");
            Connection connection;
            if (username != null) {
                connection = DriverManager.getConnection("jdbc:h2:./" + path + ";mode=MySQL;AUTO_SERVER=TRUE", username, password);
            } else {
                connection = DriverManager.getConnection("jdbc:h2:./" + path + ";mode=MySQL;AUTO_SERVER=TRUE", "sa", "");
            }
            return connection;
        } catch (ClassNotFoundException e) {
            throw new SqlException("No suitable driver");
        } catch (SQLException e) {
            throw new SqlException(e);
        }
    }

    public static Connection createSqliteConnection(String path) throws SqlException {
        File file = new File(path);
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new SqlException(e);
            }
        }
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:" + file);
        } catch (ClassNotFoundException e) {
            throw new SqlException("No suitable driver");
        } catch (SQLException e) {
            throw new SqlException(e);
        }
    }

    public static <T> T prepareStatement(Connection connection, CheckedFunction<PreparedStatement, T, SQLException> function, String query, Object... args) throws SqlException {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0, j = 1; i < args.length; i++, j++) {
                statement.setObject(j, args[i]);
            }
            return function.apply(statement);
        } catch (SQLException e) {
            throw new SqlException(e);
        }
    }

    public static <T> T executeQuery(Connection connection, CheckedFunction<ResultSet, T, SQLException> function, String query, Object... args) throws SqlException {
        return prepareStatement(connection, statement -> {
                try (ResultSet set = statement.executeQuery()) {
                    return function.apply(set);
                }
        }, query, args);
    }

    public static String escape(String str) {
        return StringUtils.escape(str, ESCAPE_CHARS);
    }

    public static String unescape(String str) {
        return StringUtils.unescape(str, ESCAPE_CHARS);
    }

    public static String escape(String str, String escape) {
        return StringUtils.escape(str, escape, ESCAPE_CHARS);
    }

    public static String unescape(String str, String escape) {
        return StringUtils.unescape(str, escape, ESCAPE_CHARS);
    }
}
