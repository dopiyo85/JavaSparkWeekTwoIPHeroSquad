package org.javasparkips.dao;

import org.javasparkips.model.Hero;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

public class HeroSquadDao {
    private final Sql2o sql2o;

    public HeroSquadDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public void addAssignment(int squadId, int heroId) {
        String sql = "INSERT INTO squads_heroes (squad_id, hero_id) VALUES (:squadId, :heroId)";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("squadId", squadId)
                    .addParameter("heroId", heroId)
                    .executeUpdate();
        }
    }

    public void removeAssignment(int squadId, int heroId) {
        String sql = "DELETE FROM squads_heroes WHERE squad_id = :squadId AND hero_id = :heroId";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("squadId", squadId)
                    .addParameter("heroId", heroId)
                    .executeUpdate();
        }
    }

    public List<Hero> getAssignedHeroes(int squadId) {
        String sql = "SELECT h.* FROM heroes h " +
                "JOIN squads_heroes sh ON h.id = sh.hero_id " +
                "WHERE sh.squad_id = :squadId";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("squadId", squadId)
                    .executeAndFetch(Hero.class);
        }
    }
}