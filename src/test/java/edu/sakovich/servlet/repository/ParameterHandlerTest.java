package edu.sakovich.servlet.repository;

import edu.sakovich.servlet.exception.RepositoryException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParameterHandlerTest {

    @Test
    void testThrowExceptionIfParameterNullByObject() {
        Object nullObject = null;
        assertThrows(RepositoryException.class, () -> ParameterHandler.throwExceptionIfParameterNull(nullObject,
                "problem"));
    }

    @Test
    void testThrowExceptionIfParameterNullByBoolean() {
        assertThrows(RepositoryException.class, () -> ParameterHandler.throwExceptionIfParameterNull(true,
                "problem"));
    }

    @Test
    void testThrowExceptionIfParameterNullByInt() {
        assertThrows(RepositoryException.class, () -> ParameterHandler.throwExceptionIfParameterNull(0,
                "problem"));
    }

    @Test
    void departmentNotExist() {
        String expected = "Department with ID = 1 doesn't exist.\nAdd such a department in the database\n";
        String actual = ParameterHandler.departmentNotExist(1);
        assertEquals(expected, actual);
    }

    @Test
    void subjectNotExist() {
        String expected = "Subject with ID = 1 doesn't exist.\nAdd such a subject in the database\n";
        String actual = ParameterHandler.subjectNotExist(1);
        assertEquals(expected, actual);
    }

    @Test
    void professorNotExist() {
        String expected = "Professor with ID = 1 doesn't exist.\nAdd such a professor in the database\n";
        String actual = ParameterHandler.professorNotExist(1);
        assertEquals(expected, actual);
    }
}