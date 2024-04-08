package edu.sakovich.servlet.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionManagerTestTest {

    @Test
    void TestGetConnection() {
        assertNotNull(new ConnectionManagerImpl().getConnection());
    }
}