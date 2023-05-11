package org.javasparkips.app.model;

public class Squad {
    private int maxSize;
    private String name;
    private String cause;

    public Squad(int maxSize, String name, String cause) {
        this.maxSize = maxSize;
        this.name = name;
        this.cause = cause;
    }

    // Getter and setter methods

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
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

