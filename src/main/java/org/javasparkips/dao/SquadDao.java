package org.javasparkips.dao;

import org.javasparkips.model.Squad;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

public class SquadDao {
    private final Sql2o sql2o;

    public SquadDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public void addSquad(Squad squad) {
        String sql = "INSERT INTO squads (name, max_size, cause) VALUES (:name, :maxSize, :cause)";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("name", squad.getName())
                    .addParameter("maxSize", squad.getMaxSize())
                    .addParameter("cause", squad.getCause())
                    .executeUpdate();
        }
    }

    public List<Squad> getAllSquads() {
        String sql = "SELECT * FROM squads";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(Squad.class);
        }
    }

    public void deleteSquad(int id) {
        String sql = "DELETE FROM squads WHERE id = :id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }

    public void updateSquad(int id, Squad squad) {
        String sql = "UPDATE squads SET name = :name, max_size = :maxSize, cause = :cause WHERE id = :id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("name", squad.getName())
                    .addParameter("maxSize", squad.getMaxSize())
                    .addParameter("cause", squad.getCause())
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }

    public Squad findSquadById(int id) {
        String sql = "SELECT * FROM squads WHERE id = :id";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Squad.class);
        }
    }

    public void assignHeroToSquad(int squadId, int heroId) {
        String sql = "INSERT INTO squads_heroes (squad_id, hero_id) VALUES (:squadId, :heroId)";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("squadId", squadId)
                    .addParameter("heroId", heroId)
                    .executeUpdate();
        }
    }

    public void removeHeroFromSquad(int squadId, int heroId) {
        String sql = "DELETE FROM squads_heroes WHERE squad_id = :squadId AND hero_id = :heroId";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("squadId", squadId)
                    .addParameter("heroId", heroId)
                    .executeUpdate();
        }
    }

    // Other methods...

}

