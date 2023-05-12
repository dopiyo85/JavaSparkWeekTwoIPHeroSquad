package org.javasparkips.app;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.javasparkips.app.model.Hero;
import org.javasparkips.app.model.Squad;
import com.google.gson.Gson;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.*;

import static spark.Spark.*;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class App {
    private static List<Hero> heroes = new ArrayList<>();
    private static List<Squad> squads = new ArrayList<>();
    private static Gson gson = new Gson();
    private static Sql2o sql2o; // SQL2O instance for database connection
    private static Handlebars handlebars; // Handlebars instance for templating

    private static Hero getHeroById(int id) {
        for (Hero hero : heroes) {
            if (hero.getId() == id) {
                return hero;
            }
        }
        return null;
    }

    private static Squad getSquadById(int id) {
        for (Squad squad : squads) {
            if (squad.getId() == id) {
                return squad;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        port(4567);

        // Load configuration
        Properties config = ConfigLoader.loadConfig();
        String dbUrl = config.getProperty("db.url");
        String dbUser = config.getProperty("db.username");
        String dbPassword = config.getProperty("db.password");

        // Set up SQL2O connection
        sql2o = new Sql2o(dbUrl, dbUser, dbPassword);

        // Initialize Handlebars
        handlebars = new Handlebars();

        // Define routes
        get("/", (request, response) -> {
            try {
                Map<String, Object> model = new HashMap<>();
                model.put("message", "Hello, Handlebars!");
                return handlebars.compile("index").apply(model);
            } catch (Exception e) {
                // Log or print the error details
                e.printStackTrace();
                // Set the appropriate response status
                response.status(500);
                return "Internal Server Error";
            }
        });

        // Routes for heroes
        path("/heroes", () -> {
            post("", (request, response) -> {
                try (Connection connection = sql2o.open()) {
                    Hero hero = gson.fromJson(request.body(), Hero.class);
                    String query = "INSERT INTO heroes (name, age, special_power, weakness) VALUES (:name, :age, :specialPower, :weakness)";
                    int heroId = connection.createQuery(query, true)
                            .addParameter("name", hero.getName())
                            .addParameter("age", hero.getAge())
                            .addParameter("specialPower", hero.getSpecialPower())
                            .addParameter("weakness", hero.getWeakness())
                            .executeUpdate()
                            .getKey(Integer.class);

                    hero.setId(heroId);
                    heroes.add(hero);
                    return gson.toJson(hero);
                }
            });

            get("", (request, response) -> gson.toJson(heroes));

            get("/:id", (request, response) -> {
                int id = Integer.parseInt(request.params("id"));
                Hero hero = getHeroById(id);
                if (hero != null) {
                    return gson.toJson(hero);
                } else {
                    response.status(404);
                    return "Hero not found";
                }
            });

            put("/:id", (request, response) -> {
                int id = Integer.parseInt(request.params("id"));
                Hero hero = getHeroById(id);
                if (hero != null) {
                    Hero updatedHero = gson.fromJson(request.body(), Hero.class);
                    hero.setName(updatedHero.getName());
                    hero.setAge(updatedHero.getAge());
                    hero.setSpecialPower(updatedHero.getSpecialPower());
                    hero.setWeakness(updatedHero.getWeakness());
                    return gson.toJson(hero);
                } else {
                    response.status(404);
                    return "Hero not found";
                }
            });

            delete("/:id", (request, response) -> {
                int id = Integer.parseInt(request.params("id"));
                Hero hero = getHeroById(id);
                if (hero != null) {
                    heroes.remove(hero);
                    try (Connection connection = sql2o.open()) {
                        String query = "DELETE FROM heroes WHERE id = :id";
                        connection.createQuery(query)
                                .addParameter("id", id)
                                .executeUpdate();
                    }
                    return "Hero deleted";
                } else {
                    response.status(404);
                    return "Hero not found";
                }
            });
        });


        // Routes for squads
        path("/squads", () -> {
            post("", (request, response) -> {
                try (org.sql2o.Connection connection = sql2o.open()) {
                    Squad squad = gson.fromJson(request.body(), Squad.class);
                    String query = "INSERT INTO squads (name, cause) VALUES (:name, :cause)";
                    int squadId = connection.createQuery(query, true)
                            .addParameter("name", squad.getName())
                            .addParameter("cause", squad.getCause())
                            .executeUpdate()
                            .getKey(Integer.class);

                    squad.setId(squadId);
                    return gson.toJson(squad);
                } catch (Exception e) {
                    response.status(500);
                    return "Failed to insert squad";
                }
            });

            get("", (request, response) -> gson.toJson(squads));

            get("/:id", (request, response) -> {
                int id = Integer.parseInt(request.params("id"));
                Squad squad = getSquadById(id);
                if (squad != null) {
                    return gson.toJson(squad);
                } else {
                    response.status(404);
                    return "Squad not found";
                }
            });

            put("/:id", (request, response) -> {
                int id = Integer.parseInt(request.params("id"));
                Squad squad = getSquadById(id);
                if (squad != null) {
                    Squad updatedSquad = gson.fromJson(request.body(), Squad.class);
                    squad.setName(updatedSquad.getName());
                    squad.setCause(updatedSquad.getCause());
                    return gson.toJson(squad);
                } else {
                    response.status(404);
                    return "Squad not found";
                }
            });

            delete("/:id", (request, response) -> {
                int id = Integer.parseInt(request.params("id"));
                Squad squad = getSquadById(id);
                if (squad != null) {
                    squads.remove(squad);
                    try (org.sql2o.Connection connection = sql2o.open()) {
                        String query = "DELETE FROM squads WHERE id = :id";
                        connection.createQuery(query)
                                .addParameter("id", id)
                                .executeUpdate();
                    } catch (Exception e) {
                        response.status(500);
                        return "Failed to delete squad";
                    }
                    return "Squad deleted";
                } else {
                    response.status(404);
                    return "Squad not found";
                }
            });
        });
    }
}