package com.example.restfulbooker.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Central configuration manager that loads values from src/test/resources/config.properties
 * and supports overriding via system properties / environment variables.
 */
public class ConfigManager {

    private static final String CONFIG_FILE = "/config.properties";
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream is = ConfigManager.class.getResourceAsStream(CONFIG_FILE)) {
            if (is != null) {
                PROPS.load(is);
            } else {
                throw new RuntimeException("Could not find " + CONFIG_FILE + " on classpath");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    private ConfigManager() {}

    public static String get(String key) {
        // 1. System property
        String sys = System.getProperty(key);
        if (sys != null && !sys.isEmpty()) {
            return sys;
        }

        // 2. Env variable
        String env = System.getenv(key);
        if (env != null && !env.isEmpty()) {
            return env;
        }

        // 3. config.properties
        String value = PROPS.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing config key: " + key);
        }
        return value;
    }

    public static String getBaseUri() {
        return get("BASE_URI");
    }

    public static String getUsername() {
        return get("BOOKER_USERNAME");
    }

    public static String getPassword() {
        return get("BOOKER_PASSWORD");
    }
}
