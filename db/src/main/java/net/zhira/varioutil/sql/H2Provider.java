package net.zhira.varioutil.sql;

import net.zhira.varioutil.util.SqlUtils;

public class H2Provider extends AbstractProvider {
    public H2Provider(String path, String username, String password) {
        super(SqlUtils.createH2Connection(path, username, password));
    }
}
