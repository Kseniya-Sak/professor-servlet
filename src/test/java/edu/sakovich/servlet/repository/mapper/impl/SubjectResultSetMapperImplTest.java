package edu.sakovich.servlet.repository.mapper.impl;

import edu.sakovich.servlet.exception.ResultSetMapperException;
import edu.sakovich.servlet.model.Department;
import edu.sakovich.servlet.model.Professor;
import edu.sakovich.servlet.model.Subject;
import edu.sakovich.servlet.repository.mapper.SubjectResultSetMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SubjectResultSetMapperImplTest {
    private static final String NAME = "testName";
    private static final int ID = 1;

    private static ResultSet mockResultSet;
    private static SubjectResultSetMapper mapper;

    @BeforeEach
    void setUp() {
        mockResultSet = mock(ResultSet.class);
        mapper = new SubjectResultSetMapperImpl();
    }

    @Test
    void testMapFromResultSetExist() throws SQLException {
        when(mockResultSet.getInt("s_id")).thenReturn(ID);
        when(mockResultSet.getString("s_name")).thenReturn(NAME);
        when(mockResultSet.getInt("value_of_hours")).thenReturn(72);

        Subject expected = new Subject(ID, NAME, 72);
        Subject actual = mapper.map(mockResultSet);

        assertEquals(expected, actual);
    }

    @Test
    void testMapFromResultSetThrowException() throws SQLException {
        when(mockResultSet.getInt("s_id")).thenThrow(SQLException.class);

        assertThrows(ResultSetMapperException.class, () -> mapper.map(mockResultSet));
    }

    @Test
    void testMapFromResultSetAndSubjectExist() throws SQLException {
        when(mockResultSet.getInt("s_id")).thenReturn(ID);
        Subject paramSubject = new Subject(NAME, 72);

        Subject expected = new Subject(ID, NAME, 72);
        Subject actual = mapper.map(mockResultSet, paramSubject);

        assertEquals(expected, actual);
    }

    @Test
    void testMapFromResultSetAndSubjectThrowException() throws SQLException {
        when(mockResultSet.getInt("s_id")).thenThrow(SQLException.class);
        assertThrows(ResultSetMapperException.class, () -> mapper.map(mockResultSet, new Subject()));
    }

    @Test
    void mapSingleResultWithSetProfessorsExist() throws SQLException {
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("s_id")).thenReturn(ID);
        when(mockResultSet.getString("s_name")).thenReturn(NAME);
        when(mockResultSet.getInt("value_of_hours")).thenReturn(72);
        when(mockResultSet.getInt("p_id")).thenReturn(ID);
        when(mockResultSet.getString("p_name")).thenReturn(NAME);
        when(mockResultSet.getString("surname")).thenReturn("testSurname");
        when(mockResultSet.getInt("d_id")).thenReturn(ID);
        when(mockResultSet.getString("d_name")).thenReturn(NAME);

        Department department = new Department(ID, NAME);
        Professor professor = new Professor(ID, NAME, "testSurname", department);

        Subject expected = new Subject(ID, NAME, 72);
        expected.setProfessors(Collections.singleton(professor));

        Optional<Subject> actualOptional = mapper.mapSingleResultWithSetProfessors(mockResultSet);

        assertAll(
                () -> assertTrue(actualOptional.isPresent()),
                () -> assertEquals(expected, actualOptional.get())
        );
    }

    @Test
    void mapSingleResultWithSetProfessorsEmpty() throws SQLException {
        when(mockResultSet.next()).thenReturn(false);
        assertTrue(mapper
                .mapSingleResultWithSetProfessors(mockResultSet)
                .isEmpty());
    }

    @Test
    void mapSingleResultWithSetProfessorsThrowException() throws SQLException {
        when(mockResultSet.next()).thenThrow(SQLException.class);

        assertThrows(ResultSetMapperException.class,
                () -> mapper.mapSingleResultWithSetProfessors(mockResultSet));
    }
}