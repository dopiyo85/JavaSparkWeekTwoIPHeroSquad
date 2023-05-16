package org.javasparkips.model;

public class Squad {
    private int id;
    private String name;
    private int max_size;
    private String cause;

    public Squad(String name, int max_size, String cause) {
        this.name = name;
        this.max_size = max_size;
        this.cause = cause;
    }

    // Add getters and setters

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

    public int getMaxSize() {
        return max_size;
    }

    public void setMaxSize(int max_size) {
        this.max_size = max_size;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}