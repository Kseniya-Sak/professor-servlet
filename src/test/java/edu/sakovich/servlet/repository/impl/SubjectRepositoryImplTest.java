package edu.sakovich.servlet.repository.impl;

import edu.sakovich.servlet.repository.ParentTest;
import edu.sakovich.servlet.db.ConnectionManagerTest;
import edu.sakovich.servlet.exception.RepositoryException;
import edu.sakovich.servlet.model.Subject;
import edu.sakovich.servlet.repository.SubjectRepository;
import edu.sakovich.servlet.repository.mapper.impl.SubjectResultSetMapperImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SubjectRepositoryImplTest extends ParentTest {
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
        jdbcDatabaseDelegate = new JdbcDatabaseDelegate(postgres, "");
    }

    @BeforeEach
    void setUp() {
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "sql/createTables.sql");
    }

    @Test
    void testSaveNotNullParameter() {
        Subject subject = new Subject("testName", 30);
        Subject savedSubject = subjectRepository.save(subject);

        assertAll(
                () -> assertNotNull(savedSubject),
                () -> assertEquals(1, savedSubject.getId()),
                () -> assertEquals("testName", savedSubject.getName()),
                () -> assertThrows(RepositoryException.class,
                        () -> subjectRepository.save(subject),
                        """
                        A subject named "%s" already exists
                        """.formatted("testName"))
        );
    }

    @Test
    void testSaveNullParameter() {
        Subject subjectNullFirstParameter = new Subject(null, 30);
        Subject subjectZeroSecondParameter = new Subject("testName", 0);

        assertAll(
                () -> assertThrows(RepositoryException.class,
                        () -> subjectRepository.save(subjectNullFirstParameter)),
                () -> assertThrows(RepositoryException.class,
                        () -> subjectRepository.save(subjectZeroSecondParameter)),
                () -> assertThrows(RepositoryException.class,
                        () -> subjectRepository.save(null))
        );
    }

    @Test
    void testUpdateNotNullParameter() {
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "sql/addData.sql");
        Subject savedSubject = new Subject(1, "Plant growing", 36);
        savedSubject.setName("newName");

        assertTrue(subjectRepository.update(savedSubject));

    }

    @Test
    void testUpdateNullParameter() {
        assertThrows(RepositoryException.class,
                () -> subjectRepository.update(null),
                "The subject must not be null");
    }

    @Test
    void testFindByIdNotNullParameter() {
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, "sql/addData.sql");

        Optional<Subject> subjectOptional = subjectRepository.findById(1);

        assertTrue(subjectOptional.isPresent());
    }

    @Test
    void testFindByIdNullParameter() {
        assertThrows(RepositoryException.class,
                () -> subjectRepository.findById(null));
    }

    @Test
    void testFindAllTableNotNull() {
        Subject subject1 = new Subject("testName1", 31);
        Subject subject2 = new Subject("testName2", 32);
        Subject subject3 = new Subject("testName3", 33);

        subjectRepository.save(subject1);
        subjectRepository.save(subject2);
        subjectRepository.save(subject3);


        Set<Subject> subjects = subjectRepository.findAll();

        assertEquals(3, subjects.size());
    }

    @Test
    void testFindAllTableIsEmpty() {
        assertTrue(subjectRepository.findAll().isEmpty());
    }

    @Test
    void testDeleteByIdNotNullParameter() {
        Subject subject = new Subject("testName", 30);
        Subject savedSubject = subjectRepository.save(subject);

        assertAll(
                () -> assertEquals(1, subjectRepository.findAll().size()),
                () -> assertTrue(subjectRepository.deleteById(savedSubject.getId())),
                () -> assertTrue(subjectRepository.findAll().isEmpty())
        );
    }

    @Test
    void testDeleteByIdNullParameter() {
        assertThrows(RepositoryException.class,
                () -> subjectRepository.deleteById(null));
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