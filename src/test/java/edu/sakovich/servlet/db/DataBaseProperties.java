package edu.sakovich.servlet.db;

import java.io.FileInputStream;
import java.util.Properties;

public class DataBaseProperties {
    private static final Properties PROPERTIES = new Properties();
    private static final String PROPERTIES_FILE = "src\\test\\resources\\db.properties";

    static {
        try (FileInputStream inputStream = new FileInputStream(PROPERTIES_FILE)) {
            PROPERTIES.load(inputStream);
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    private DataBaseProperties() {
    }

    public static String getProperties(String key) {
        return PROPERTIES.getProperty(key);
    }

}
