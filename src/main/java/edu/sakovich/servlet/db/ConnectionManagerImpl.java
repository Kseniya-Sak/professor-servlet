package edu.sakovich.servlet.db;

import edu.sakovich.servlet.exception.DataBaseConnectionException;
import edu.sakovich.servlet.exception.DataBaseDriverLoadException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static edu.sakovich.servlet.db.DataBaseProperties.getProperties;

public class ConnectionManagerImpl implements ConnectionManager {
    private static final String DRIVER = "db.driver";
    private static final String URL = "db.url";
    private static final String USERNAME = "db.username";
    private static final String PASSWORD = "db.password";

    public ConnectionManagerImpl() {
    }

    public Connection getConnection()  {
        try {
                Class.forName(getProperties(DRIVER));
            } catch (ClassNotFoundException e) {
                throw new DataBaseDriverLoadException("Database driver didn't load.");
            }
        try {
                return DriverManager.getConnection(getProperties(URL),
                        getProperties(USERNAME), getProperties(PASSWORD)
                );
            } catch (SQLException e) {
                throw new DataBaseConnectionException(
                        "Problem with connection. Check URL, USERNAME, PASSWORD");
            }
    }

}
