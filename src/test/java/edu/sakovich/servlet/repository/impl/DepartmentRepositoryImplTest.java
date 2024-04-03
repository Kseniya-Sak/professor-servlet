package edu.sakovich.servlet.repository.impl;

import edu.sakovich.servlet.ParentTest;
import edu.sakovich.servlet.db.ConnectionManagerTest;
import edu.sakovich.servlet.exception.RepositoryException;
import edu.sakovich.servlet.model.Department;
import edu.sakovich.servlet.repository.DepartmentRepository;
import edu.sakovich.servlet.repository.mapper.impl.DepartmentResultSetMapperImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentRepositoryImplTest extends ParentTest {
    private static DepartmentRepository departmentRepository;
    private static JdbcDatabaseDelegate jdbcDatabaseDelegate;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        ConnectionManagerTest connectionManager = new ConnectionManagerTest(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
        departmentRepository = new DepartmentRepositoryImpl(connectionManager, new DepartmentResultSetMapperImpl());
        jdbcDatabaseDelegate = new JdbcDatabaseDelegate(postgres, "");
    }

    @BeforeEach
    void setUp() {
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "sql/createTables.sql");
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

    @BeforeEach
    void reset() {
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "sql/dropTables.sql");
    }


    @AfterAll
    static void afterAll() {
        postgres.stop();
    }
}