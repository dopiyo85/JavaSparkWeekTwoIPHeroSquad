package org.javasparkips.app.model;

import org.sql2o.Sql2o;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Hero {

    private int id;
    private String name;
    private int age;
    private String specialPower;
    private String weakness;

    public Hero(String name, int age, String specialPower, String weakness) {
        this.name = name;
        this.age = age;
        this.specialPower = specialPower;
        this.weakness = weakness;
    }

    // Getter and setter methods

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSpecialPower() {
        return specialPower;
    }

    public void setSpecialPower(String specialPower) {
        this.specialPower = specialPower;
    }

    public String getWeakness() {
        return weakness;
    }

    public void setWeakness(String weakness) {
        this.weakness = weakness;
    }

    // Other methods

    public void createNewHero(Sql2o sql2o) {
        String query = "INSERT INTO heroes (name, age, special_power, weakness) VALUES (?, ?, ?, ?)";

        try (java.sql.Connection connection = sql2o.open().getJdbcConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, specialPower);
            statement.setString(4, weakness);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
