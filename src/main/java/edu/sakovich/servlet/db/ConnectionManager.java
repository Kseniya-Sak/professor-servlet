package edu.sakovich.servlet.db;

import java.sql.Connection;

public interface ConnectionManager {
    Connection getConnection();
}
