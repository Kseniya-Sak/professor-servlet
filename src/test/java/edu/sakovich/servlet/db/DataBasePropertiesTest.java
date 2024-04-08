package edu.sakovich.servlet.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataBasePropertiesTest {

    @Test
    void testGetPropertiesSuccess() {
        assertEquals("my_db", DataBaseProperties.getProperties("db.databaseName"));
    }
}