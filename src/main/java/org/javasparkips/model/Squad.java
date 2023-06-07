package org.javasparkips.model;

public class Squad {
    private int id;
    private String name;
    private int maxSize;
    private String cause;

    public Squad(String name, int maxSize, String cause) {
        this.name = name;
        this.maxSize = maxSize;
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
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}