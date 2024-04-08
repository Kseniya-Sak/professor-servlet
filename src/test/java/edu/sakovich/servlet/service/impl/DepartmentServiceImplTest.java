package edu.sakovich.servlet.service.impl;

import edu.sakovich.servlet.exception.NotFoundException;
import edu.sakovich.servlet.model.Department;
import edu.sakovich.servlet.model.Professor;
import edu.sakovich.servlet.repository.DepartmentRepository;
import edu.sakovich.servlet.service.DepartmentService;
import edu.sakovich.servlet.servlet.dto.DepartmentIncomingDto;
import edu.sakovich.servlet.servlet.dto.DepartmentOutGoingDto;
import edu.sakovich.servlet.servlet.dto.FindByIdDepartmentOutGoingDto;
import edu.sakovich.servlet.servlet.mapper.DepartmentDtoMapper;
import edu.sakovich.servlet.servlet.mapper.DepartmentDtoMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DepartmentServiceImplTest {
    DepartmentRepository mockitoDepartmentRepository;
    DepartmentDtoMapper departmentDtoMapper;
    DepartmentService departmentService;

    @BeforeEach
    void setUp() {
        mockitoDepartmentRepository = Mockito.mock(DepartmentRepository.class);
        departmentDtoMapper = new DepartmentDtoMapperImpl();
        departmentService = new DepartmentServiceImpl(mockitoDepartmentRepository, departmentDtoMapper);
    }

    @Test
    void testSave() throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter("test");
        Department wantToSaveDepartment = new Department("testName");
        Department savedDepartment = new Department(1, "testName");
        Mockito.doReturn(savedDepartment).when(mockitoDepartmentRepository).save(wantToSaveDepartment);

        DepartmentIncomingDto wantToSaveDepartmentIncomingDto = new DepartmentIncomingDto();
        wantToSaveDepartmentIncomingDto.setName("testName");
        DepartmentOutGoingDto actualResult = departmentService.save(wantToSaveDepartmentIncomingDto);

        DepartmentOutGoingDto expectedResult = new DepartmentOutGoingDto();
        expectedResult.setId(1);
        expectedResult.setName("testName");

        assertAll(
                () -> assertEquals(expectedResult.getId(), actualResult.getId()),
                () -> assertEquals(expectedResult.getName(), actualResult.getName())
        );
    }

    @Test
    void testUpdate() {
        Department updateDepartment = new Department(1, "newTestName");
        Mockito.doReturn(true).when(mockitoDepartmentRepository).update(updateDepartment);

        DepartmentIncomingDto updateDepartmentIncomingDto = new DepartmentIncomingDto();
        updateDepartmentIncomingDto.setId(updateDepartment.getId());
        updateDepartmentIncomingDto.setName(updateDepartment.getName());

        assertTrue(departmentService.update(updateDepartmentIncomingDto));
    }

    @Test
    void findByIdExists() {
        int id = 1;
        Department receivedDepartment = new Department(id, "testName");
        Set<Professor> professors = new LinkedHashSet<>();
        professors.add(new Professor(1, "testName", "testSurname", receivedDepartment));
        receivedDepartment.setProfessors(professors);

        Optional<Department> receivedDepartmentOptional = Optional.of(receivedDepartment);
        Mockito.doReturn(receivedDepartmentOptional).when(mockitoDepartmentRepository).findById(id);

        FindByIdDepartmentOutGoingDto actualResult = departmentService.findById(id);

        FindByIdDepartmentOutGoingDto expectedResult = new FindByIdDepartmentOutGoingDto();
        expectedResult.setId(id);
        expectedResult.setName("testName");

        assertAll(
                () -> assertEquals(expectedResult.getId(), actualResult.getId()),
                () -> assertEquals(expectedResult.getName(), actualResult.getName())
        );
    }

    @Test
    void findByIdNotExists() {
        int id = 1;
        Optional<Department> departmentOptional = Optional.empty();
        Mockito.doReturn(departmentOptional).when(mockitoDepartmentRepository).findById(id);
        assertThrows(NotFoundException.class, () -> departmentService.findById(id));
    }

    @Test
    void findAll() {
        Set<Department> receivedDepartments = new LinkedHashSet<>();
        receivedDepartments.add(new Department(1, "testName1"));
        receivedDepartments.add(new Department(2, "testName2"));
        receivedDepartments.add(new Department(3, "testName3"));
        Mockito.doReturn(receivedDepartments).when(mockitoDepartmentRepository).findAll();

        Set<DepartmentOutGoingDto> actualResult = departmentService.findAll();

        Set<DepartmentOutGoingDto> expectedResult = new LinkedHashSet<>();
        for (Department receivedDepartment : receivedDepartments) {
            expectedResult.add(departmentDtoMapper.map(receivedDepartment));
        }

        assertEquals(expectedResult.size(), actualResult.size());
    }

    @Test
    void deleteByIdDepartmentExists() {
        int id = 1;
        Mockito.doReturn(true).when(mockitoDepartmentRepository).deleteById(id);

        boolean actualResult = departmentService.deleteById(id);
        boolean expectedResult = true;

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void deleteByIdDepartmentNotExists() {
        int id = 1;
        Mockito.doReturn(false).when(mockitoDepartmentRepository).deleteById(id);

        assertThrows(NotFoundException.class, () -> departmentService.deleteById(id));
    }
}