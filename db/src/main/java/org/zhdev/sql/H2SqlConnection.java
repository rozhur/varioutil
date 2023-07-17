package org.zhdev.sql;

import org.zhdev.util.SqlUtils;

public final class H2SqlConnection extends AbstractSqlConnection {
    public H2SqlConnection(String path, String username, String password) {
        super(SqlUtils.createH2Connection(path, username, password));
    }
}
