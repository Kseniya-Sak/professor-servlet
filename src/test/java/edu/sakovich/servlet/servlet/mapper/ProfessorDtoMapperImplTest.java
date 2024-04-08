package edu.sakovich.servlet.servlet.mapper;

import edu.sakovich.servlet.model.Department;
import edu.sakovich.servlet.model.Professor;
import edu.sakovich.servlet.model.Subject;
import edu.sakovich.servlet.servlet.dto.DepartmentIncomingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorIncomingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorOutGoingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorWithSubjectsIncomingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorWithSubjectsOutGoingDto;
import edu.sakovich.servlet.servlet.dto.SubjectIncomingDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProfessorDtoMapperImplTest {
    private ProfessorDtoMapper professorDtoMapper;

    @BeforeEach
    void setUp() {
        professorDtoMapper = new ProfessorDtoMapperImpl();
    }

    @Test
    void testMapToProfessorOutGoingDto() {
        Department department = new Department();
        department.setId(1);
        department.setName("testDepartment");

        Professor professor = new Professor();
        professor.setId(1);
        professor.setName("testName");
        professor.setSurname("testSurname");
        professor.setDepartment(department);

        ProfessorOutGoingDto actual = professorDtoMapper.map(professor);

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(1, actual.getId()),
                () -> assertEquals("testName", actual.getName()),
                () -> assertEquals("testDepartment", actual.getDepartment().getName())
        );

    }

    @Test
    void testMapToProfessor() {
        DepartmentIncomingDto departmentIncomingDto = new DepartmentIncomingDto();
        departmentIncomingDto.setId(1);
        departmentIncomingDto.setName("testDepartment");

        ProfessorIncomingDto professorIncomingDto = new ProfessorIncomingDto();
        professorIncomingDto.setId(1);
        professorIncomingDto.setName("testName");
        professorIncomingDto.setSurname("testSurname");
        professorIncomingDto.setDepartmentIncomingDto(departmentIncomingDto);

        Professor actual = professorDtoMapper.map(professorIncomingDto);

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(1, actual.getId()),
                () -> assertEquals("testName", actual.getName()),
                () -> assertEquals("testDepartment", actual.getDepartment().getName())
        );
    }

    @Test
    void testMapProfessorWithSubjectsOutGoingDtoToOutGoingDto() {
        Department department = new Department();
        department.setId(1);
        department.setName("testDepartment");

        Subject subject = new Subject(1, "testName", 62);

        Professor professor = new Professor(1, "testName", "testSurname", department,
                new LinkedHashSet<>(Collections.singleton(subject)));

        ProfessorWithSubjectsOutGoingDto actual = professorDtoMapper.mapProfessorWithSubjectsOutGoingDto(professor);

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(1, actual.getId()),
                () -> assertEquals("testName", actual.getName()),
                () -> assertEquals("testDepartment", actual.getDepartment().getName())
        );

    }

    @Test
    void testMapProfessorWithSubjectsOutGoingDtoToProfessor() {
        DepartmentIncomingDto departmentIncomingDto = new DepartmentIncomingDto();
        departmentIncomingDto.setId(1);
        departmentIncomingDto.setName("testDepartment");

        SubjectIncomingDto subjectIncomingDto = new SubjectIncomingDto();
        subjectIncomingDto.setId(1);
        subjectIncomingDto.setName("testName");
        subjectIncomingDto.setValueOfHours(62);

        ProfessorWithSubjectsIncomingDto professorIncomingDto = new ProfessorWithSubjectsIncomingDto();
        professorIncomingDto.setId(1);
        professorIncomingDto.setName("testName");
        professorIncomingDto.setSurname("testSurname");
        professorIncomingDto.setDepartment(departmentIncomingDto);
        professorIncomingDto.setSubjects(Collections.singleton(subjectIncomingDto));

        Professor actual = professorDtoMapper.mapProfessorWithSubjectsOutGoingDto(professorIncomingDto);

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(1, actual.getId()),
                () -> assertEquals("testName", actual.getName()),
                () -> assertEquals("testDepartment", actual.getDepartment().getName())
        );
    }
}