package org.javasparkips.app;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final String CONFIG_FILE = "config.properties";

    public static Properties loadConfig() {
        Properties properties = new Properties();
        try (InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }
}

