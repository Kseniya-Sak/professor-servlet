package edu.sakovich.servlet.repository.impl.department;

import com.zaxxer.hikari.HikariDataSource;
import edu.sakovich.servlet.db.ConnectionManagerImpl;
import edu.sakovich.servlet.exception.RepositoryException;
import edu.sakovich.servlet.model.Department;
import edu.sakovich.servlet.repository.DepartmentRepository;
import edu.sakovich.servlet.repository.impl.DepartmentRepositoryImpl;
import edu.sakovich.servlet.repository.mapper.impl.DepartmentResultSetMapperImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;

import java.sql.SQLException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentRepositoryImplTest {
    private static HikariDataSource dataSource = new HikariDataSource();
    private static DepartmentRepository departmentRepository;
    private static JdbcDatabaseDelegate jdbcDatabaseDelegate;
    private static ConnectionManagerImpl connectionManager;

    @Container
    public static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("my_db")
                    .withUsername("postgres")
                    .withPassword("12345");
//                    .withExposedPorts(5432);

    @BeforeAll
    static void beforeAll() throws SQLException {
        postgres.start();
        System.out.println(dataSource.isClosed());
        dataSource.setDriverClassName(postgres.getDriverClassName());
        dataSource.setJdbcUrl(postgres.getJdbcUrl());
        dataSource.setUsername(postgres.getUsername());
        dataSource.setPassword(postgres.getPassword());
//        dataSource.setMinimumIdle(100);
//        dataSource.setMaximumPoolSize(1000);
//        dataSource.setConnectionTimeout(5);
////        dataSource.setMaxLifetime(1);
//        dataSource.setAutoCommit(true);
//        dataSource.setLoginTimeout(10);

        jdbcDatabaseDelegate = new JdbcDatabaseDelegate(postgres, "");
    }



    @BeforeEach
    void setUp() {
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "sql/createTables.sql");
        connectionManager = ConnectionManagerImpl.getInstance(dataSource);
        departmentRepository = new DepartmentRepositoryImpl(connectionManager, new DepartmentResultSetMapperImpl());
    }

    @Test
    void testSaveNotNullParameter() {
        Department department = new Department("Test1");
        Department savedDepartment = departmentRepository.save(department);

        assertAll(
                () -> assertNotNull(savedDepartment),
                () -> assertEquals(1, savedDepartment.getId()),
                () -> assertEquals("Test1", savedDepartment.getName()),
                () -> assertThrows(RepositoryException.class,
                        () -> departmentRepository.save(department),
                        """
                        A department named "%s" already exists
                        """.formatted("Test1"))
        );
    }

    @Test
    void testSaveNullParameter() {
        Department department = new Department(null);
        assertAll(
                () -> assertThrows(RepositoryException.class,
                        () -> departmentRepository.save(department),
                        "The department's name must not be null"),
                () -> assertThrows(RepositoryException.class,
                        () -> departmentRepository.save(null),
                        "The department must not be null")
        );
    }

    @Test
    void testSaveNameAlreadyExist() {
        Department department = new Department("test");
        departmentRepository.save(department);

        assertThrows(RepositoryException.class, () -> departmentRepository.save(department));
    }

    @Test
    void testUpdateNotNullParameter() {
        Department department = new Department("Test1");
        Department savedDepartment = departmentRepository.save(department);

                    savedDepartment.setName("Test2");
        assertTrue(departmentRepository.update(savedDepartment));
    }

    @Test
    void testUpdateNullParameter() {
        Department department = new Department(null);
        assertAll(
                () -> assertThrows(RepositoryException.class,
                        () -> departmentRepository.update(department),
                        "The department's name must not be null"),
                () -> assertThrows(RepositoryException.class,
                        () -> departmentRepository.update(null),
                        "The department must not be null")
        );
    }

    @Test
    void testFindByIdNullParameter() {
        assertThrows(RepositoryException.class,
                        () -> departmentRepository.findById(null));
    }

    @Test
    void testFindAllTableNotNull() {
        Department department1 = new Department("Test1");
        Department department2 = new Department("Test2");
        Department department3 = new Department("Test3");

        departmentRepository.save(department1);
        departmentRepository.save(department2);
        departmentRepository.save(department3);

        Set<Department> departments = departmentRepository.findAll();

        assertEquals(3, departments.size());
    }

    @Test
    void testFindAllTableIsEmpty() {
        assertTrue(departmentRepository.findAll().isEmpty());
    }

    @Test
    void testDeleteByIdNotNullParameter() {
        Department department = new Department("Test1");
        Department savedDepartment = departmentRepository.save(department);

        assertAll(
                () -> assertEquals(1, departmentRepository.findAll().size()),
                () -> assertTrue(departmentRepository.deleteById(savedDepartment.getId())),
                () -> assertTrue(departmentRepository.findAll().isEmpty())
        );
    }

    @Test
    void testDeleteByIdNullParameter() {
        assertThrows(RepositoryException.class,
                () -> departmentRepository.deleteById(null));
    }

    @AfterEach
    void reset() {
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "sql/dropTables.sql");
    }


    @AfterAll
    static void afterAll() {
        connectionManager = null;
        postgres.stop();
    }
}