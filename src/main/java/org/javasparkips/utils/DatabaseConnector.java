package org.javasparkips.utils;

import org.sql2o.Sql2o;

public class DatabaseConnector {
    private final String url;
    private final String username;
    private final String password;
    private Sql2o sql2o;

    public DatabaseConnector(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void init() {
        sql2o = new Sql2o(url, username, password);
    }

    public Sql2o getSql2o() {
        return sql2o;
    }
}
