import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.javasparkips.app.model.Hero;
import org.javasparkips.app.model.Squad;
import com.google.gson.Gson;
import org.sql2o.Sql2o;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class App {
    private static List<Hero> heroes = new ArrayList<>();
    private static List<Squad> squads = new ArrayList<>();
    private static Gson gson = new Gson();
    private static Sql2o sql2o; // SQL2O instance for database connection
    private static Handlebars handlebars; // Handlebars instance for templating

    public static void main(String[] args) {
        port(4567);

        // Set up SQL2O connection
        String dbUrl = "jdbc:postgresql://localhost:5432/your-database-name";
        String dbUser = "your-username";
        String dbPassword = "your-password";Glorified30*
        sql2o = new Sql2o(dbUrl, dbUser, dbPassword);

        // Initialize Handlebars
        handlebars = new Handlebars();

        // Routes for heroes
        path("/heroes", () -> {
            post("", (request, response) -> {
                try (Connection con = sql2o.open()) {
                    Hero hero = gson.fromJson(request.body(), Hero.class);
                    String query = "INSERT INTO heroes (name, age, special_power, weakness) VALUES (:name, :age, :specialPower, :weakness)";
                    try (PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                        stmt.setString("name", hero.getName());
                        stmt.setInt("age", hero.getAge());
                        stmt.setString("specialPower", hero.getSpecialPower());
                        stmt.setString("weakness", hero.getWeakness());
                        stmt.executeUpdate();

                        ResultSet generatedKeys = stmt.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            int id = generatedKeys.getInt(1);
                            hero.setId(id);
                            heroes.add(hero);
                            return gson.toJson(hero);
                        } else {
                            response.status(500);
                            return "Failed to insert hero";
                        }
                    }
                } catch (SQLException e) {
                    response.status(500);
                    return "Failed to insert hero";
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
                    return "Hero deleted";
                }
