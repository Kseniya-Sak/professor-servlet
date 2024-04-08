package edu.sakovich.servlet.repository.mapper.impl;

import edu.sakovich.servlet.exception.ResultSetMapperException;
import edu.sakovich.servlet.model.Department;
import edu.sakovich.servlet.model.Professor;
import edu.sakovich.servlet.repository.mapper.DepartmentResultSetMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class DepartmentResultSetMapperImplTest {
    private static final String TEST = "test";
    private static final int ID = 1;
    private static ResultSet mockResultSet;
    private static DepartmentResultSetMapper mapper;

    @BeforeEach
    void setUp() {
        mockResultSet = Mockito.mock(ResultSet.class);
        mapper = new DepartmentResultSetMapperImpl();
    }

    @Test
    void mapToDepartment() throws SQLException {
        when(mockResultSet.getInt("d_id")).thenReturn(ID);
        when((mockResultSet.getString("d_name"))).thenReturn(TEST);

        Department expected = new Department(ID, TEST);
        Department actual = mapper.map(mockResultSet);

        assertEquals(expected, actual);
    }

    @Test
    void mapToDepartmentThrowException() throws SQLException {
        when(mockResultSet.getInt("d_id")).thenThrow(SQLException.class);

        assertThrows(ResultSetMapperException.class, () -> mapper.map(mockResultSet));
    }

    @Test
    void MapToDepartmentFromDepartment() throws SQLException {
        when(mockResultSet.getInt("d_id")).thenReturn(ID);
        Department paramDepartment = new Department(TEST);

        Department expected = new Department(ID, TEST);
        Department actual = mapper.map(mockResultSet, paramDepartment);

        assertEquals(expected, actual);
    }

    @Test
    void MapToDepartmentFromDepartmentThrowException() throws SQLException {
        when(mockResultSet.getInt("d_id")).thenThrow(SQLException.class);
        Department paramDepartment = new Department(TEST);

        assertThrows(ResultSetMapperException.class, () -> mapper.map(mockResultSet, paramDepartment));
    }

    @Test
    void mapSingleResultWithSetProfessorsExist() throws SQLException {
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("d_id")).thenReturn(ID);
        when(mockResultSet.getString("d_name")).thenReturn(TEST);
        when(mockResultSet.getInt("p_id")).thenReturn(ID);
        when(mockResultSet.getString("p_name")).thenReturn("testName");
        when(mockResultSet.getString("surname")).thenReturn("testSurname");
        Professor professor = new Professor(ID, "testName", "testSurname");

        Optional<Department> expected = Optional.of(new Department(ID, TEST,
                new LinkedHashSet<>(Collections.singleton(professor))));

        Optional<Department> actual = mapper.mapSingleResultWithSetProfessors(mockResultSet);
        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertEquals(expected.get().getProfessors(), actual.get().getProfessors())
        );
    }

    @Test
    void mapSingleResultWithSetProfessorsEmpty() throws SQLException {
        when(mockResultSet.next()).thenReturn(false);

        Optional<Department> actual = mapper.mapSingleResultWithSetProfessors(mockResultSet);

        assertTrue(actual.isEmpty());
    }


    @Test
    void mapSingleResultWithSetProfessorsThrowException() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("d_id")).thenReturn(ID);
        when(mockResultSet.getString("d_name")).thenReturn(TEST);
        when(mockResultSet.getInt("p_id")).thenReturn(ID);
        when(mockResultSet.getString("p_name")).thenReturn("testName");
        when(mockResultSet.getString("surname")).thenThrow(SQLException.class);

        assertThrows(ResultSetMapperException.class,
                () -> mapper.mapSingleResultWithSetProfessors(mockResultSet));
    }
}