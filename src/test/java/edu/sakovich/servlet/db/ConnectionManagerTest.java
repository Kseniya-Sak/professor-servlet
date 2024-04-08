package edu.sakovich.servlet.db;

import edu.sakovich.servlet.exception.RepositoryException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManagerTest implements ConnectionManager {
    private final String URL;
    private final String USERNAME;
    private final String PASSWORD;

    private static Connection connection;

    public ConnectionManagerTest(String url, String username, String password) {
        URL = url;
        USERNAME = username;
        PASSWORD = password;
    }

    public Connection getConnection()  {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RepositoryException(
                    "Problem with connection. Check URL, USERNAME, PASSWORD");
        }
    }

}
