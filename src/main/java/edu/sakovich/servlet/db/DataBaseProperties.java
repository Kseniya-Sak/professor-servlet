package edu.sakovich.servlet.db;

import java.io.InputStream;
import java.util.Properties;

public class DataBaseProperties {
    private static final Properties PROPERTIES = new Properties();
    private static final String PROPERTIES_FILE = "db.properties";


    private DataBaseProperties() {
    }

    public static String getProperties(String key) {
        try (InputStream inputStream = DataBaseProperties.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            PROPERTIES.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
        return PROPERTIES.getProperty(key);
    }

}
