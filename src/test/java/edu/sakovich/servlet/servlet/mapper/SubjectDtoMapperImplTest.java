package edu.sakovich.servlet.servlet.mapper;

import edu.sakovich.servlet.model.Department;
import edu.sakovich.servlet.model.Professor;
import edu.sakovich.servlet.model.Subject;
import edu.sakovich.servlet.servlet.dto.SubjectWithProfessorsOutGoingDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class SubjectDtoMapperImplTest {
    private SubjectDtoMapper subjectDtoMapper;

    @BeforeEach
    void setUp() {
        subjectDtoMapper = new SubjectDtoMapperImpl();
    }

    @Test
    void mapToWithProfessorsOutGoingDto() {
        Department department = new Department();
        department.setId(1);
        department.setName("testDepartment");

        Professor professor = new Professor(1, "testName", "testSurname", department);

        Subject subject = new Subject(1, "testName", 62, Collections.singleton(professor));

        SubjectWithProfessorsOutGoingDto actual = subjectDtoMapper.mapToWithProfessorsOutGoingDto(subject);

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(1, actual.getId()),
                () -> assertEquals("testName", actual.getName()),
                () -> assertNotNull(actual.getProfessors())
        );
    }
}