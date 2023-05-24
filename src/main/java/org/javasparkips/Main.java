package org.javasparkips;

import org.javasparkips.dao.HeroDao;
import org.javasparkips.dao.SquadDao;
import org.javasparkips.model.Hero;
import org.javasparkips.model.Squad;
import org.javasparkips.utils.DatabaseConnector;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        // Set the static files location
        staticFileLocation("/public");

        // Set up the Handlebars template engine
        HandlebarsTemplateEngine templateEngine = new HandlebarsTemplateEngine();

        // Set up the database connector
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.init();

        // Set up the HeroDao and SquadDao
        HeroDao heroDao = new HeroDao(databaseConnector.getSql2o());
        SquadDao squadDao = new SquadDao(databaseConnector.getSql2o());

        // Home page route
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("heroes", heroDao.getAllHeroes());
            model.put("squads", squadDao.getAllSquads());
            return new ModelAndView(model, "index.hbs");
        }, templateEngine);

        // Create a new hero route
        post("/heroes", (req, res) -> {
            String name = req.queryParams("name");
            int age = Integer.parseInt(req.queryParams("age"));
            String specialPower = req.queryParams("specialPower");
            String weakness = req.queryParams("weakness");

            Hero hero = new Hero(name, age, specialPower, weakness);
            heroDao.addHero(hero);

            res.redirect("/");
            return null;
        });

        // Create a new squad route
        post("/squads", (req, res) -> {
            String name = req.queryParams("name");
            int maxSize = Integer.parseInt(req.queryParams("maxSize"));
            String cause = req.queryParams("cause");

            Squad squad = new Squad(name, maxSize, cause);
            squadDao.addSquad(squad);

            res.redirect("/");
            return null;
        });

        // Assign a hero to a squad route
        post("/squads/:squadId/heroes/:heroId", (req, res) -> {
            int squadId = Integer.parseInt(req.params("squadId"));
            int heroId = Integer.parseInt(req.params("heroId"));

            squadDao.assignHeroToSquad(squadId, heroId);

            res.redirect("/");
            return null;
        });

        // Serve static CSS file
        get("/css/styles.css", (req, res) -> {
            res.type("text/css");
            return Main.class.getResourceAsStream("/css/styles.css");
        });

        // Serve static JS file
        get("/js/main.js", (req, res) -> {
            res.type("text/javascript");
            return Main.class.getResourceAsStream("/js/main.js");
        });
    }
}
