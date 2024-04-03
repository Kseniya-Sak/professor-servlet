package edu.sakovich.servlet.service.impl;

import edu.sakovich.servlet.exception.NotFoundException;
import edu.sakovich.servlet.model.Department;
import edu.sakovich.servlet.model.Professor;
import edu.sakovich.servlet.model.Subject;
import edu.sakovich.servlet.repository.ProfessorRepository;
import edu.sakovich.servlet.service.ProfessorService;
import edu.sakovich.servlet.servlet.dto.DepartmentIncomingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorWithSubjectsIncomingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorWithSubjectsOutGoingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorIncomingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorOutGoingDto;
import edu.sakovich.servlet.servlet.dto.SubjectIncomingDto;
import edu.sakovich.servlet.servlet.mapper.DepartmentDtoMapper;
import edu.sakovich.servlet.servlet.mapper.DepartmentDtoMapperImpl;
import edu.sakovich.servlet.servlet.mapper.ProfessorDtoMapper;
import edu.sakovich.servlet.servlet.mapper.ProfessorDtoMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorServiceImplTest {

    ProfessorRepository mockitoProfessorRepository;
    ProfessorDtoMapper professorDtoMapper;
    ProfessorService professorService;
    DepartmentDtoMapper departmentDtoMapper;

    @BeforeEach
    void setUp() {
        mockitoProfessorRepository = Mockito.mock(ProfessorRepository.class);
        professorDtoMapper = new ProfessorDtoMapperImpl();
        professorService = new ProfessorServiceImpl(mockitoProfessorRepository, professorDtoMapper);
        departmentDtoMapper = new DepartmentDtoMapperImpl();
    }

    @Test
    void testSave() {
        Department savedDepartment = new Department(1, "testName");
        Professor wantToSaveProfessor = new Professor("testName", "testSurname", savedDepartment);

        Professor savedProfessor = new Professor(1, "testName", "testSurname", savedDepartment);
        Set<Subject> subjects = new LinkedHashSet<>();
        subjects.add(new Subject(1, "test", 32));
        savedProfessor.setSubjects(subjects);
        Mockito.doReturn(savedProfessor).when(mockitoProfessorRepository).save(wantToSaveProfessor);

        ProfessorWithSubjectsIncomingDto wantToSaveProfessorIncomingDto = new ProfessorWithSubjectsIncomingDto();
        Set<SubjectIncomingDto> subjectIncomingDtos = new LinkedHashSet<>();
        SubjectIncomingDto subjectIncomingDto = new SubjectIncomingDto();
        subjectIncomingDto.setId(1);
        subjectIncomingDto.setName("test");
        subjectIncomingDto.setValueOfHours(32);

        subjectIncomingDtos.add(subjectIncomingDto);

        wantToSaveProfessorIncomingDto.setSubjects(subjectIncomingDtos);

        wantToSaveProfessorIncomingDto.setName("testName");
        wantToSaveProfessorIncomingDto.setSurname("testSurname");

        DepartmentIncomingDto savedDepartmentIncomingDto = new DepartmentIncomingDto();
        savedDepartmentIncomingDto.setId(savedDepartment.getId());
        savedDepartmentIncomingDto.setName(savedDepartment.getName());

        wantToSaveProfessorIncomingDto.setDepartment(savedDepartmentIncomingDto);

        ProfessorWithSubjectsOutGoingDto actualResult = professorService.save(wantToSaveProfessorIncomingDto);

        ProfessorOutGoingDto expectedResult = new ProfessorOutGoingDto();
        expectedResult.setId(1);
        expectedResult.setName("testName");
        expectedResult.setSurname("testSurname");
        expectedResult.setDepartment(departmentDtoMapper.map(savedDepartment));

        assertAll(
                () -> assertEquals(expectedResult.getId(), actualResult.getId()),
                () -> assertEquals(expectedResult.getName(), actualResult.getName()),
                () -> assertEquals(expectedResult.getSurname(), actualResult.getSurname()),
                () -> assertEquals(expectedResult.getDepartment().getId(),
                        actualResult.getDepartment().getId())
        );
    }

    @Test
    void testUpdate() {
        Department savedDepartment = new Department(1, "testName");

        Professor updateProfessor = new Professor(1, "newTestName", "newTestSurname", savedDepartment);

        Mockito.doReturn(true).when(mockitoProfessorRepository).update(updateProfessor);

        ProfessorWithSubjectsIncomingDto wantToUpdateProfessorIncomingDto = new ProfessorWithSubjectsIncomingDto();
        wantToUpdateProfessorIncomingDto.setId(1);
        wantToUpdateProfessorIncomingDto.setName("newTestName");
        wantToUpdateProfessorIncomingDto.setSurname("newTestSurname");

        DepartmentIncomingDto savedDepartmentIncomingDto = new DepartmentIncomingDto();
        savedDepartmentIncomingDto.setId(1);
        savedDepartmentIncomingDto.setName("testName");

        wantToUpdateProfessorIncomingDto.setDepartment(savedDepartmentIncomingDto);

        Set<SubjectIncomingDto> subjectIncomingDtos = new LinkedHashSet<>();
        SubjectIncomingDto subjectIncomingDto = new SubjectIncomingDto();
        subjectIncomingDto.setId(1);
        subjectIncomingDto.setName("test");
        subjectIncomingDto.setValueOfHours(32);

        wantToUpdateProfessorIncomingDto.setSubjects(subjectIncomingDtos);

        assertTrue(professorService.update(wantToUpdateProfessorIncomingDto));
    }

    @Test
    void findByIdExists() {
        int id = 1;
        Department savedDepartment = new Department(1, "testName");
        Professor receivedProfessor = new Professor(id, "testName", "testSurname", savedDepartment);

        Set<Subject> subjects = new LinkedHashSet<>();
        subjects.add(new Subject(1, "test", 32));
        receivedProfessor.setSubjects(subjects);

        Optional<Professor> receivedProfessorOptional = Optional.of(receivedProfessor);
        Mockito.doReturn(receivedProfessorOptional).when(mockitoProfessorRepository).findById(id);

        ProfessorWithSubjectsOutGoingDto actualResult = professorService.findById(id);

        ProfessorWithSubjectsOutGoingDto expectedResult = new ProfessorWithSubjectsOutGoingDto();
        expectedResult.setId(id);
        expectedResult.setName("testName");
        expectedResult.setSurname("testSurname");
        expectedResult.setDepartment(departmentDtoMapper.map(savedDepartment));

        assertAll(
                () -> assertEquals(expectedResult.getId(), actualResult.getId()),
                () -> assertEquals(expectedResult.getName(), actualResult.getName()),
                () -> assertEquals(expectedResult.getSurname(), actualResult.getSurname()),
                () -> assertEquals(expectedResult.getDepartment().getId(),
                        actualResult.getDepartment().getId())
        );
    }

    @Test
    void findByIdNotExists() {
        int id = 1;
        Optional<Professor> professorOptional = Optional.empty();
        Mockito.doReturn(professorOptional).when(mockitoProfessorRepository).findById(id);

        assertThrows(NotFoundException.class, () -> professorService.findById(id));
    }

    @Test
    void findAll() {
        Department savedDepartment = new Department(1, "testName");

        Set<Professor> receivedProfessors = new LinkedHashSet<>();
        receivedProfessors.add(new Professor(1, "testName1", "testSurname1", savedDepartment));
        receivedProfessors.add(new Professor(2, "testName2", "testSurname2", savedDepartment));
        receivedProfessors.add(new Professor(3, "testName3", "testSurname3", savedDepartment));

        Mockito.doReturn(receivedProfessors).when(mockitoProfessorRepository).findAll();

        Set<ProfessorOutGoingDto> actualResult = professorService.findAll();

        Set<ProfessorOutGoingDto> expectedResult = new LinkedHashSet<>();
        for (Professor receivedProfessor : receivedProfessors) {
            expectedResult.add(professorDtoMapper.map(receivedProfessor));
        }

        assertEquals(expectedResult.size(), actualResult.size());
    }

    @Test
    void deleteByIdDepartmentExists() {
        int id = 1;
        Mockito.doReturn(true).when(mockitoProfessorRepository).deleteById(id);

        boolean actualResult = professorService.deleteById(id);
        boolean expectedResult = true;

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void deleteByIdDepartmentNotExists() {
        int id = 1;
        Mockito.doReturn(false).when(mockitoProfessorRepository).deleteById(id);

        assertThrows(NotFoundException.class, () -> professorService.deleteById(id));
    }

}