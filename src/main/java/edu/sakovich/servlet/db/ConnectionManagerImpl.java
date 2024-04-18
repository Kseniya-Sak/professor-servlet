package edu.sakovich.servlet.db;

import com.zaxxer.hikari.HikariDataSource;
import edu.sakovich.servlet.exception.RepositoryException;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManagerImpl implements ConnectionManager {

    private final HikariDataSource dataSource;

    private ConnectionManagerImpl(HikariDataSource dataSource) {
        this.dataSource=dataSource;
    }

    public static ConnectionManagerImpl getInstance(HikariDataSource dataSource) {
       return new ConnectionManagerImpl(dataSource);
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }
}
