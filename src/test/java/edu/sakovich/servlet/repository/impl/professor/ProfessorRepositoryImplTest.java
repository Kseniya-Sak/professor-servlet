package edu.sakovich.servlet.repository.impl.professor;

import com.zaxxer.hikari.HikariDataSource;
import edu.sakovich.servlet.db.ConnectionManagerImpl;
import edu.sakovich.servlet.exception.RepositoryException;
import edu.sakovich.servlet.model.Department;
import edu.sakovich.servlet.model.Professor;
import edu.sakovich.servlet.model.Subject;
import edu.sakovich.servlet.repository.DepartmentRepository;
import edu.sakovich.servlet.repository.ProfessorRepository;
import edu.sakovich.servlet.repository.SubjectRepository;
import edu.sakovich.servlet.repository.impl.DepartmentRepositoryImpl;
import edu.sakovich.servlet.repository.impl.ProfessorRepositoryImpl;
import edu.sakovich.servlet.repository.impl.SubjectRepositoryImpl;
import edu.sakovich.servlet.repository.mapper.impl.DepartmentResultSetMapperImpl;
import edu.sakovich.servlet.repository.mapper.impl.ProfessorResultSetMapperImpl;
import edu.sakovich.servlet.repository.mapper.impl.SubjectResultSetMapperImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class ProfessorRepositoryImplTest  {
    private static final HikariDataSource dataSource = new HikariDataSource();
    private static DepartmentRepository departmentRepository;
    private static ProfessorRepository professorRepository;
    private static SubjectRepository subjectRepository;
    private static JdbcDatabaseDelegate jdbcDatabaseDelegate;
    private static ConnectionManagerImpl connectionManager;

    @Container
    public static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("my_db")
                    .withUsername("postgres")
                    .withPassword("12345");

    @BeforeAll
    static void beforeAll() throws SQLException {
        postgres.start();
        System.out.println(dataSource.isClosed());
        dataSource.setDriverClassName(postgres.getDriverClassName());
        dataSource.setJdbcUrl(postgres.getJdbcUrl());
        dataSource.setUsername(postgres.getUsername());
        dataSource.setPassword(postgres.getPassword());

        jdbcDatabaseDelegate = new JdbcDatabaseDelegate(postgres, "");
    }


    @BeforeEach
    void setUp() {
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "sql/createTables.sql");
        connectionManager = ConnectionManagerImpl.getInstance(dataSource);
        subjectRepository = new SubjectRepositoryImpl(connectionManager, new SubjectResultSetMapperImpl());
        departmentRepository = new DepartmentRepositoryImpl(connectionManager, new DepartmentResultSetMapperImpl());
        professorRepository = new ProfessorRepositoryImpl(connectionManager, new ProfessorResultSetMapperImpl());
    }

    @Test
    void testSaveNotNullParameterDepartmentExist() {
        Subject subject = subjectRepository.save(new Subject("testName", 72));
        Department savedDepartment = departmentRepository.save(new Department( "testDepartment"));
        Professor professor = new Professor("testName", "testSurname", savedDepartment,
                new LinkedHashSet<>(Collections.singleton(subject)));
        Professor savedProfessor = professorRepository.save(professor);
        System.out.println(savedProfessor);
        assertAll(
                () -> assertNotNull(savedProfessor),
                () -> assertEquals(1, savedProfessor.getId()),
                () -> assertEquals("testName", savedProfessor.getName()),
                () -> assertEquals("testSurname", savedProfessor.getSurname()),
                () -> assertEquals(savedDepartment, professor.getDepartment()),
                () -> assertThrows(RepositoryException.class,
                        () -> professorRepository.save(professor))
        );
    }

    @Test
    void testSaveNotNullParameterDepartmentIsNotExist() {
        Department department = new Department("testDepartment");
        Professor professor = new Professor("testName", "testSurname", department);

        assertThrows(RepositoryException.class, () -> professorRepository.save(professor));
    }

    @Test
    void testSaveProfessorWithSuchNameAlreadyExist() {
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "sql/addData.sql");

        Professor professor = new Professor(1, "Marina", "Chigareva",
                new Department(1, "Biology department"));


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
    void testFindAllTableNotNull() {
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "sql/addData.sql");
        Set<Professor> professors = professorRepository.findAll();

        assertEquals(16, professors.size());
    }

    @Test
    void testFindAllTableIsEmpty() {
        assertTrue(professorRepository.findAll().isEmpty());
    }

    @Test
    void testFindByIdNotNullParameter() {
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "sql/addData.sql");
        Optional<Professor> foundProfessorOptional = professorRepository.findById(1);

        assertTrue(foundProfessorOptional.isPresent());
    }

    @Test
    void testFindByIdNullParameter() {
        assertThrows(RepositoryException.class,
                () -> professorRepository.findById(null));
    }

    @Test
    void testUpdateNotNullParameter() {
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "sql/addData.sql");
        Professor savedProfessor = new Professor(1, "Marina", "Chigareva",
                new Department(1, "Biology department"),
                new LinkedHashSet<>(Collections.singletonList(new Subject(1, "Plant growing", 36))));

        assertTrue(professorRepository.update(savedProfessor));
    }

    @Test
    void testUpdateNullParameter() {
        assertThrows(RepositoryException.class,
                () -> departmentRepository.update(null),
                "The department must not be null");
    }

    @Test
    void testDeleteByIdNotNullParameter() {
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "sql/addData.sql");

        assertTrue(professorRepository.deleteById(1));
    }

    @Test
    void testDeleteByIdNullParameter() {
        assertThrows(RepositoryException.class,
                () -> departmentRepository.deleteById(null));
    }

    @AfterEach
    void reset()
    {
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "sql/dropTables.sql");
    }

    @AfterAll
    static void afterAll() {
        connectionManager = null;
        postgres.stop();
    }
}