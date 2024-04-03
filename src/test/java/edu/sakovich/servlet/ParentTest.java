package edu.sakovich.servlet;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;

import static edu.sakovich.servlet.db.DataBaseProperties.getProperties;

public class ParentTest {
    private static final String DOCKER_IMGE_NAME = "db.dockerImageName";
    private static final String DATABASE_NAME = "db.databaseName";
    private static final String USERNAME = "db.username";
    private static final String PASSWORD = "db.password";
    private static int containerPort = 5432;
    private static int localPort = 5432;

    @Container
    public static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(getProperties(DOCKER_IMGE_NAME))
            .withDatabaseName(getProperties(DATABASE_NAME))
            .withUsername(getProperties(USERNAME))
            .withPassword(getProperties(PASSWORD))
            .withExposedPorts(containerPort);
//            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
//                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(localPort), new ExposedPort(containerPort)))
//            ))
//            .withInitScript("sql/allQuery.sql");

}
