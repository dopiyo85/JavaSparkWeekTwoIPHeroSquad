package org.javasparkips.app.model;

public class Squad {
    private final static int maxSize = 10;
    private String name;
    private String cause;

    public Squad(String name, String cause) {
        this.name = name;
        this.cause = cause;
    }

    // Getter and setter methods

    public int getMaxSize() {
        return maxSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
