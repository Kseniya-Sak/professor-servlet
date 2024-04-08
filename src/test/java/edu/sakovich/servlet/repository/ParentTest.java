package edu.sakovich.servlet.repository;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static edu.sakovich.servlet.db.DataBaseProperties.getProperties;

public class ParentTest {
    private static final String DOCKER_IMGE_NAME = "db.dockerImageName";
    private static final String DATABASE_NAME = "db.databaseName";
    private static final String USERNAME = "db.username";
    private static final String PASSWORD = "db.password";
    private static final int CONTAINER_PORT = 5432;

    @Container
    public static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(getProperties(DOCKER_IMGE_NAME))
            .withDatabaseName(getProperties(DATABASE_NAME))
            .withUsername(getProperties(USERNAME))
            .withPassword(getProperties(PASSWORD))
            .withExposedPorts(CONTAINER_PORT);
}
