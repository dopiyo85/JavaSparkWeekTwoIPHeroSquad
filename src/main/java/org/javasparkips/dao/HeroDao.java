package org.javasparkips.dao;

import org.javasparkips.model.Hero;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.ArrayList;
import java.util.List;

public class HeroDao {
    private final Sql2o sql2o;

    public HeroDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public List<Hero> getAllHeroes() {
        List<Hero> heroes = new ArrayList<>();
        try (Connection connection = sql2o.open()) {
            String query = "SELECT * FROM heroes";
            heroes = connection.createQuery(query).executeAndFetch(Hero.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return heroes;
    }

    public void addHero(Hero hero) {
        try (Connection connection = sql2o.open()) {
            String query = "INSERT INTO heroes (name, age, power, weakness) VALUES (:name, :age, :power, :weakness)";
            connection.createQuery(query)
                    .addParameter("name", hero.getName())
                    .addParameter("age", hero.getAge())
                    .addParameter("power", hero.getPower())
                    .addParameter("weakness", hero.getWeakness())
                    .executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateHero(Hero hero) {
        try (Connection connection = sql2o.open()) {
            String query = "UPDATE heroes SET name = :name, age = :age, power = :power, weakness = :weakness WHERE id = :id";
            connection.createQuery(query)
                    .addParameter("name", hero.getName())
                    .addParameter("age", hero.getAge())
                    .addParameter("power", hero.getPower())
                    .addParameter("weakness", hero.getWeakness())
                    .addParameter("id", hero.getId())
                    .executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteHero(int id) {
        try (Connection connection = sql2o.open()) {
            String query = "DELETE FROM heroes WHERE id = :id";
            connection.createQuery(query)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Hero findHeroById(int id) {
        Hero hero = null;
        try (Connection connection = sql2o.open()) {
            String query = "SELECT * FROM heroes WHERE id = :id";
            hero = connection.createQuery(query)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Hero.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hero;
    }

    // Other methods for retrieving heroes by criteria, finding heroes by name, etc.
}