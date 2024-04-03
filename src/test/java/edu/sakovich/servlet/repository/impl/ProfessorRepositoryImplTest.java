package edu.sakovich.servlet.repository.impl;

import edu.sakovich.servlet.ParentTest;
import edu.sakovich.servlet.db.ConnectionManagerTest;
import edu.sakovich.servlet.exception.RepositoryException;
import edu.sakovich.servlet.model.Department;
import edu.sakovich.servlet.model.Professor;
import edu.sakovich.servlet.repository.DepartmentRepository;
import edu.sakovich.servlet.repository.ProfessorRepository;
import edu.sakovich.servlet.repository.SubjectRepository;
import edu.sakovich.servlet.repository.mapper.impl.DepartmentResultSetMapperImpl;
import edu.sakovich.servlet.repository.mapper.impl.ProfessorResultSetMapperImpl;
import edu.sakovich.servlet.repository.mapper.impl.SubjectResultSetMapperImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorRepositoryImplTest extends ParentTest {
    private static DepartmentRepository departmentRepository;
    private static ProfessorRepository professorRepository;
    private static SubjectRepository subjectRepository;
    private static JdbcDatabaseDelegate jdbcDatabaseDelegate;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        ConnectionManagerTest connectionManager = new ConnectionManagerTest(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
        subjectRepository = new SubjectRepositoryImpl(connectionManager, new SubjectResultSetMapperImpl());
        departmentRepository = new DepartmentRepositoryImpl(connectionManager, new DepartmentResultSetMapperImpl());
        professorRepository = new ProfessorRepositoryImpl(connectionManager, new ProfessorResultSetMapperImpl(),
                departmentRepository, subjectRepository);
        jdbcDatabaseDelegate = new JdbcDatabaseDelegate(postgres, "");
    }

    @BeforeEach
    void setUp() {
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "sql/createTables.sql");
    }

    @Test
    void testSaveNotNullParameterDepartmentIsNotExist() {
        Department department = new Department("testDepartment");
        Professor professor = new Professor("testName", "testSurname", department);

        assertThrows(RepositoryException.class, () -> professorRepository.save(professor));
    }

    @Test
    void testSaveNullNameParameter() {
        Department department = new Department("testDepartment");
        Department savedDepartment = departmentRepository.save(department);
        Professor professor = new Professor(null, "testSurname", savedDepartment);
        assertAll(
                () -> assertThrows(RepositoryException.class,
                        () -> professorRepository.save(professor)),
                () -> assertThrows(RepositoryException.class,
                        () -> professorRepository.save(null))
        );
    }

    @Test
    void testSaveNullSurnameParameter() {
        Department department = new Department("testDepartment");
        Department savedDepartment = departmentRepository.save(department);
        Professor professor = new Professor("testSurname", null, savedDepartment);
        assertAll(
                () -> assertThrows(RepositoryException.class,
                        () -> professorRepository.save(professor)),
                () -> assertThrows(RepositoryException.class,
                        () -> professorRepository.save(null))
        );
    }

    @Test
    void testSaveNullDepartmentParameter() {
        Professor professor = new Professor("testSurname", "testSurname", null);
        assertAll(
                () -> assertThrows(RepositoryException.class,
                        () -> professorRepository.save(professor)),
                () -> assertThrows(RepositoryException.class,
                        () -> professorRepository.save(null))
        );
    }

    @Test
    void testFindAllTableIsEmpty() {
        assertTrue(professorRepository.findAll().isEmpty());
    }

    @Test
    void testFindByIdNullParameter() {
        assertThrows(RepositoryException.class,
                () -> professorRepository.findById(null));
    }

    @Test
    void testUpdateNullParameter() {
        assertThrows(RepositoryException.class,
                () -> departmentRepository.update(null),
                "The department must not be null");
    }

    @Test
    void testDeleteByIdNullParameter() {
        assertThrows(RepositoryException.class,
                () -> departmentRepository.deleteById(null));
    }

    @Test
    void exitsById() {
    }

    @AfterEach
    void reset() {
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "sql/dropTables.sql");
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

}