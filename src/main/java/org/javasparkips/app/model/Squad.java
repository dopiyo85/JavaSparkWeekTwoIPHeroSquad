package org.javasparkips.app.model;

import org.sql2o.Sql2o;
import org.sql2o.Connection;

public class Squad {
    private final static int maxSize = 5;
    private int id;
    private String name;
    private String cause;

    public Squad(String name, String cause) {
        this.name = name;
        this.cause = cause;
    }

    // Getter and setter methods

    public int getMaxSize() {
        return maxSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    // Other methods

    public static Squad getSquadById(int id, Sql2o sql2o) {
        String query = "SELECT * FROM squads WHERE id = :id";

        try (Connection connection = sql2o.open()) {
            return connection.createQuery(query)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Squad.class);
        }
    }
}
