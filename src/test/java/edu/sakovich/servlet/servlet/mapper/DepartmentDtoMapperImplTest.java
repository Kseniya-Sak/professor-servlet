package edu.sakovich.servlet.servlet.mapper;

import edu.sakovich.servlet.model.Department;
import edu.sakovich.servlet.model.Professor;
import edu.sakovich.servlet.servlet.dto.DepartmentIncomingDto;
import edu.sakovich.servlet.servlet.dto.DepartmentOutGoingDto;
import edu.sakovich.servlet.servlet.dto.FindByIdDepartmentOutGoingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorShortOutGoingDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentDtoMapperImplTest {
    private DepartmentDtoMapper departmentDtoMapper;

    @BeforeEach
    void setUp() {
        departmentDtoMapper = new DepartmentDtoMapperImpl();
    }

    @Test
    void mapToDepartment() {
        DepartmentIncomingDto incomingDto = new DepartmentIncomingDto();
        incomingDto.setId(1);
        incomingDto.setName("test");

        Department expected = new Department(1, "test");
        Department actual = departmentDtoMapper.map(incomingDto);

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertEquals(expected.getName(), actual.getName())
        );
    }

    @Test
    void testMapToDepartmentOutGoingDto() {
        Department department = new Department(1, "test");

        DepartmentOutGoingDto expected = new DepartmentOutGoingDto();
        expected.setId(1);
        expected.setName("test");

        DepartmentOutGoingDto actual = departmentDtoMapper.map(department);

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertEquals(expected.getName(), actual.getName())
        );
    }

    @Test
    void mapToFindByIdDepartmentOutGoingDto() {
        Department department = new Department(1, "test");
        Set<Professor> professors = new LinkedHashSet<>();
        professors.add(new Professor(1, "testName", "testSurname", department));

        department.setProfessors(professors);

        FindByIdDepartmentOutGoingDto expected = new FindByIdDepartmentOutGoingDto();
        expected.setId(1);
        expected.setName("test");
        Set<ProfessorShortOutGoingDto> professorShortOutGoingDtos = new LinkedHashSet<>();
        ProfessorShortOutGoingDto professorShortOutGoingDto = new ProfessorShortOutGoingDto();
        professorShortOutGoingDto.setId(1);
        professorShortOutGoingDto.setName("testName");
        professorShortOutGoingDto.setSurname("testSurname");
        professorShortOutGoingDtos.add(professorShortOutGoingDto);
        expected.setProfessors(professorShortOutGoingDtos);

        FindByIdDepartmentOutGoingDto actual = departmentDtoMapper.mapToFindByIdDepartmentOutGoingDto(department);

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertNotNull(expected.getProfessors()),
                () -> assertNotNull(actual.getProfessors())
        );
    }
}