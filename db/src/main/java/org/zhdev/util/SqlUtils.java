package org.zhdev.util;

import org.zhdev.sql.SqlException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class SqlUtils {
    public static final char[] ESCAPE_CHARS = {'%', '_', '[', ']', '^'};

    private static boolean sqlite;
    private static boolean h2;
    private static boolean mysql;

    private static String encodeUrlValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }

    public static Connection createMysqlConnection(String address, String dbname, String username, String password, boolean ssl) throws SqlException {
        try {
            String[] addressParts = address.split(":", 2);
            int port;
            if (addressParts.length > 1) {
                port = Integer.parseInt(encodeUrlValue(addressParts[1]));
                address = addressParts[0];
            } else {
                port = 3306;
            }
            return DriverManager.getConnection("jdbc:mysql://" + encodeUrlValue(address) + ':' + port + '/' + encodeUrlValue(dbname) + "?useSSL=" + ssl + "&autoReconnect=true", username, password);
        } catch (SQLException | UnsupportedEncodingException e) {
            throw new SqlException(e);
        }
    }

    public static Connection createH2Connection(String path, String username, String password) throws SqlException {
        try {
            Connection connection;
            if (username != null) {
                connection = DriverManager.getConnection("jdbc:h2:./" + path + ";mode=MySQL;AUTO_SERVER=TRUE", username, password);
            } else {
                connection = DriverManager.getConnection("jdbc:h2:./" + path + ";mode=MySQL;AUTO_SERVER=TRUE", "sa", "");
            }
            return connection;
        } catch (NoClassDefFoundError e) {
            throw new SqlException("No suitable driver");
        } catch (SQLException e) {
            throw new SqlException(e);
        }
    }

    public static Connection createSqliteConnection(String pathname) throws SqlException {
        Path path = Paths.get(pathname);
        if (!Files.exists(path)) {
            Path parent = path.getParent();
            try {
                if (parent != null) {
                    Files.createDirectories(parent);
                }
                Files.createFile(path);
            } catch (IOException e) {
                throw new SqlException(e);
            }
        }
        try {
            return DriverManager.getConnection("jdbc:sqlite:" + path);
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

    private static void initializeH2() {
        if (h2) {
            return;
        }

        try {
            DriverManager.registerDriver(new org.h2.Driver());
        } catch (NoClassDefFoundError | SQLException ignored) {}

        h2 = true;
    }

    private static void initializeMySql() {
        if (mysql) {
            return;
        }

        try {
            try {
                DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            } catch (NoClassDefFoundError e) {
                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            }
        } catch (NoClassDefFoundError | SQLException ignored) {}

        mysql = true;
    }

    private static void initializeSqlite() {
        if (sqlite) {
            return;
        }

        try {
            DriverManager.registerDriver(new org.sqlite.JDBC());
        } catch (NoClassDefFoundError | SQLException ignored) {}

        sqlite = true;
    }

    static {
        initializeH2();
        initializeMySql();
        initializeSqlite();
    }
}
