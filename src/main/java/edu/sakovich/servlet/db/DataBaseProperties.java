package edu.sakovich.servlet.db;

import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseProperties {

    private static final Properties PROPERTIES=new Properties();
    public static final HikariDataSource dataSource=new HikariDataSource();
    private static final String PROPERTIES_FILE ="db.properties";

    static {
        try(InputStream inputStream= DataBaseProperties.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)){
            PROPERTIES.load(inputStream);
            dataSource.setDriverClassName(PROPERTIES.getProperty("db.driver"));
            dataSource.setJdbcUrl(PROPERTIES.getProperty("db.url"));
            dataSource.setUsername(PROPERTIES.getProperty("db.username"));
            dataSource.setPassword(PROPERTIES.getProperty("db.password"));
            dataSource.setMinimumIdle(5);
            dataSource.setMaximumPoolSize(1000);
            dataSource.setAutoCommit(true);
            dataSource.setLoginTimeout(10);
        }catch (IOException | SQLException e){
            throw new ExceptionInInitializerError("Failed to read configuration fail"+e);
        }
    }

}
