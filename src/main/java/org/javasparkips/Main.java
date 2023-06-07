package org.javasparkips;

import org.javasparkips.dao.HeroDao;
import org.javasparkips.dao.HeroSquadDao;
import org.javasparkips.dao.SquadDao;
import org.javasparkips.model.Hero;
import org.javasparkips.model.Squad;
import org.javasparkips.utils.DatabaseConnector;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        // Set the static files location
        staticFileLocation("/");

        // Set up the Handlebars template engine
        HandlebarsTemplateEngine templateEngine = new HandlebarsTemplateEngine("/templates");

        // Set up the database connector
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.init();

        // Set up the HeroDao, SquadDao, and HeroSquadDao
        HeroDao heroDao = new HeroDao(databaseConnector.getSql2o());
        SquadDao squadDao = new SquadDao(databaseConnector.getSql2o());
        HeroSquadDao heroSquadDao = new HeroSquadDao(databaseConnector.getSql2o());

        // Home page route
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("heroes", heroDao.getAllHeroes());
            model.put("squads", squadDao.getAllSquads());
            return new ModelAndView(model, "index.hbs");
        }, templateEngine);

        // Add a squad route
        get("/squads", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("squads", squadDao.getAllSquads());
            return new ModelAndView(model, "squads.hbs");
        }, templateEngine);

        post("/squads/add/", (req, res) -> {
            String name = req.queryParams("name");
            String maxSizeStr = req.queryParams("maxSize");
            String cause = req.queryParams("cause");
            int maxSize;

            try {
                maxSize = Integer.parseInt(maxSizeStr);
            } catch (NumberFormatException e) {
                // Handle the error here
                Map<String, Object> model = new HashMap<>();
                model.put("error", "Invalid max size value");
                return new ModelAndView(model, "error.hbs");
            }

            Squad squad = new Squad(name, maxSize, cause);
            squadDao.addSquad(squad);

            res.redirect("/");
            return null;
        });

        // Add a hero route
        get("/heroes/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("heroes", heroDao.getAllHeroes());
            return new ModelAndView(model, "heroes.hbs");
        }, templateEngine);

        // Handle form submission for adding a hero
        post("/heroes/add/", (req, res) -> {
            String name = req.queryParams("name");
            String ageParam = req.queryParams("age");
            String power = req.queryParams("power");
            String weakness = req.queryParams("weakness");

            int age;
            try {
                age = Integer.parseInt(ageParam);
            } catch (NumberFormatException e) {
                res.redirect("/heroes?error=invalid_age");
                return null;
            }

            Hero hero = new Hero(name, age, power, weakness);
            heroDao.addHero(hero);

            res.redirect("/");
            return null;
        });

        // Handle form submission for deleting a hero
        post("/heroes/delete/:id", (req, res) -> {
            int heroId = Integer.parseInt(req.params(":id"));
            heroDao.deleteHero(heroId);

            res.redirect("/");
            return null;
        });

        // Route handler for rendering the squads page
        get("/squads", (request, response) -> {
            List<Squad> squads = squadDao.getAllSquads();
            List<Hero> availableHeroes = heroDao.getAllHeroes();
            Map<String, Object> model = new HashMap<>();
            model.put("squads", squads);
            model.put("availableHeroes", availableHeroes);
            return new ModelAndView(model, "squads.hbs");
        }, templateEngine);

        // Route handler for rendering the heroes page
        get("/heroes", (request, response) -> {
            List<Hero> heroes = heroDao.getAllHeroes();
            Map<String, Object> model = new HashMap<>();
            model.put("heroes", heroes);
            return new ModelAndView(model, "heroes.hbs");
        }, templateEngine);


        // API endpoints
        path("/api", () -> {
            // Endpoint to assign a hero to a squad
            post("/api/squads/:squadId/heroes/:heroId", (request, response) -> {
                int squadId = Integer.parseInt(request.params("squadId"));
                int heroId = Integer.parseInt(request.params("heroId"));
                heroDao.assignHeroToSquad(heroId, squadId);
                heroSquadDao.addAssignment(squadId, heroId);
                response.status(200);
                return "Hero assigned to squad.";
            });

            // Endpoint to remove a hero from a squad
            delete("/api/squads/:squadId/heroes/:heroId", (request, response) -> {
                int squadId = Integer.parseInt(request.params("squadId"));
                int heroId = Integer.parseInt(request.params("heroId"));
                heroDao.removeHeroFromSquad(heroId);
                heroSquadDao.removeAssignment(squadId, heroId);
                response.status(200);
                return "Hero removed from squad.";
            });

            // Endpoint to get assigned heroes for a squad
            get("/api/squads/:squadId/heroes", (request, response) -> {
                int squadId = Integer.parseInt(request.params("squadId"));
                List<Hero> assignedHeroes = heroSquadDao.getAssignedHeroes(squadId);
                Map<String, Object> model = new HashMap<>();
                model.put("heroes", assignedHeroes);
                return new ModelAndView(model, "heroes.hbs");
            }, templateEngine);
        });

        // Route handler for editing a hero
        get("/heroes/edit/:id", (req, res) -> {
            int heroId = Integer.parseInt(req.params(":id"));
            Hero hero = heroDao.findHeroById(heroId);

            if (hero != null) {
                Map<String, Object> model = new HashMap<>();
                model.put("hero", hero);
                model.put("id", heroId);
                return new ModelAndView(model, "heroes.hbs");
            } else {
                // Hero not found, handle the error or redirect to an error page
                res.redirect("/error");
                return null;
            }
        }, templateEngine);

        post("/heroes/edit/:id", (req, res) -> {
            int heroId = Integer.parseInt(req.params(":id"));

            // Retrieve the updated hero details from the form
            String name = req.queryParams("name");
            String ageParam = req.queryParams("age");
            String power = req.queryParams("power");
            String weakness = req.queryParams("weakness");

            // Perform any necessary validation or error handling
            if (name == null || ageParam == null || power == null || weakness == null) {
                // Handle the error here
                Map<String, Object> model = new HashMap<>();
                model.put("error", "Invalid form data");
                return new ModelAndView(model, "error.hbs");
            }

            // Convert age to an integer
            int age;
            try {
                age = Integer.parseInt(ageParam);
            } catch (NumberFormatException e) {
                // Handle the error here
                Map<String, Object> model = new HashMap<>();
                model.put("error", "Invalid age value");
                return new ModelAndView(model, "error.hbs");
            }

            // Update the hero details in the database
            Hero hero = heroDao.findHeroById(heroId);
            if (hero != null) {
                hero.setName(name);
                hero.setAge(age);
                hero.setPower(power);
                hero.setWeakness(weakness);
                heroDao.updateHero(hero);
            }

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
