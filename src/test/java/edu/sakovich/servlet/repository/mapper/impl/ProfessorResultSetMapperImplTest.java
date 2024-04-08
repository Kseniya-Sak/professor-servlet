package edu.sakovich.servlet.repository.mapper.impl;

import edu.sakovich.servlet.exception.ResultSetMapperException;
import edu.sakovich.servlet.model.Department;
import edu.sakovich.servlet.model.Professor;
import edu.sakovich.servlet.model.Subject;
import edu.sakovich.servlet.repository.mapper.ProfessorResultSetMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProfessorResultSetMapperImplTest {
    private static final String NAME = "testName";
    private static final String SURNAME = "testSurname";
    private static final int ID = 1;
    private static ResultSet mockResultSet;
    private static ProfessorResultSetMapper mapper;

    @BeforeEach
    void setUp() {
        mockResultSet = mock(ResultSet.class);
        mapper = new ProfessorResultSetMapperImpl();
    }


    @Test
    void testMapParamDepartmentExist() throws SQLException {
        when(mockResultSet.getInt("p_id")).thenReturn(ID);
        when(mockResultSet.getString("p_name")).thenReturn(NAME);
        when(mockResultSet.getString("surname")).thenReturn(SURNAME);

        Department paramDepartment = new Department(ID, "test");

        Professor expected = new Professor(ID, NAME, SURNAME, paramDepartment);
        Professor actual = mapper.map(mockResultSet, paramDepartment);

        assertEquals(expected, actual);
    }

    @Test
    void testMapParamDepartmentThrowException() throws SQLException {
        when(mockResultSet.getInt("p_id")).thenThrow(SQLException.class);

        Department paramDepartment = new Department(ID, "test");
        assertThrows(ResultSetMapperException.class, () -> mapper.map(mockResultSet, paramDepartment));
    }

    @Test
    void testMapParamProfessorExist() throws SQLException {
        when(mockResultSet.getInt("p_id")).thenReturn(ID);
        Department department = new Department(ID, NAME);
        Professor paramProfessor = new Professor(NAME, SURNAME, department);

        Professor expected = new Professor(ID, NAME, SURNAME, department);
        Professor actual = mapper.map(mockResultSet, paramProfessor);

        assertEquals(expected, actual);
    }

    @Test
    void testMapParamProfessorThrowException() throws SQLException {
        when(mockResultSet.getInt("p_id")).thenThrow(SQLException.class);

        assertThrows(ResultSetMapperException.class, () -> mapper.map(mockResultSet, new Professor()));
    }

    @Test
    void testMapSingleResultWithSetSubjectsWithIdAndProfessorParameters() {
        Department department = new Department(ID, NAME);
        Subject subject = new Subject(ID, NAME, 72);
        Professor paramProfessor = new Professor(NAME, SURNAME,
                department, new LinkedHashSet<>(Collections.singleton(subject)));

        Professor expected = new Professor(ID, NAME, SURNAME,
                department, new LinkedHashSet<>(Collections.singleton(subject)));
        Professor actual = mapper.mapSingleResultWithSetSubjects(ID, paramProfessor);

        assertEquals(expected, actual);
    }

    @Test
    void testMapSingleResultWithSetSubjectsFromResultSetExist() throws SQLException {
        when(mockResultSet.next()).thenReturn(true)
                .thenReturn(false);
        when(mockResultSet.getInt("d_id")).thenReturn(ID);
        when(mockResultSet.getString("d_name")).thenReturn(NAME);
        when(mockResultSet.getInt("p_id")).thenReturn(ID);
        when(mockResultSet.getString("p_name")).thenReturn(NAME);
        when(mockResultSet.getString("surname")).thenReturn(SURNAME);
        when(mockResultSet.getInt("s_id")).thenReturn(ID);
        when(mockResultSet.getString("s_name")).thenReturn(NAME);
        when(mockResultSet.getInt("value_of_hours")).thenReturn(72);

        Department department = new Department(ID, NAME);
        Subject subject = new Subject(ID, NAME, 72);
        Professor expected = new Professor(ID, NAME, SURNAME,
                department, new LinkedHashSet<>(Collections.singleton(subject)));

        Optional<Professor> actualOption = mapper.mapSingleResultWithSetSubjects(mockResultSet);


        assertAll(
                () -> assertTrue(actualOption.isPresent()),
                () -> assertEquals(expected, actualOption.get())
        );

    }

    @Test
    void testMapSingleResultWithSetSubjectsFromResultSetEmpty() throws SQLException {
        when(mockResultSet.next()).thenReturn(false);

        Optional<Professor> actualOption = mapper.mapSingleResultWithSetSubjects(mockResultSet);

        assertTrue(actualOption.isEmpty());
    }

    @Test
    void testMapSingleResultWithSetSubjectsFromResultSetThrowException() throws SQLException {
        when(mockResultSet.next()).thenThrow(SQLException.class);

        assertThrows(ResultSetMapperException.class,
                () -> mapper.mapSingleResultWithSetSubjects(mockResultSet));
    }
}