package edu.sakovich.servlet.service.impl;

import edu.sakovich.servlet.exception.NotFoundException;
import edu.sakovich.servlet.model.Department;
import edu.sakovich.servlet.model.Professor;
import edu.sakovich.servlet.model.Subject;
import edu.sakovich.servlet.repository.SubjectRepository;
import edu.sakovich.servlet.service.SubjectService;
import edu.sakovich.servlet.servlet.dto.SubjectIncomingDto;
import edu.sakovich.servlet.servlet.dto.SubjectOutGoingDto;
import edu.sakovich.servlet.servlet.dto.SubjectWithProfessorsOutGoingDto;
import edu.sakovich.servlet.servlet.mapper.SubjectDtoMapper;
import edu.sakovich.servlet.servlet.mapper.SubjectDtoMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SubjectServiceImplTest {
    SubjectRepository mockitoSubjectRepository;
    SubjectDtoMapper subjectDtoMapper;
    SubjectService subjectService;

    @BeforeEach
    void setUp() {
        mockitoSubjectRepository = Mockito.mock(SubjectRepository.class);
        subjectDtoMapper = new SubjectDtoMapperImpl();
        subjectService = new SubjectServiceImpl(mockitoSubjectRepository, subjectDtoMapper);
    }

    @Test
    void testSave() {
        Subject wantToSaveSubject = new Subject("testName", 30);
        Subject savedSubject = new Subject(1, "testName", 30);
        Mockito.doReturn(savedSubject).when(mockitoSubjectRepository).save(wantToSaveSubject);

        SubjectIncomingDto wantToSaveSubjectIncomingDto = new SubjectIncomingDto();
        wantToSaveSubjectIncomingDto.setName("testName");
        wantToSaveSubjectIncomingDto.setValueOfHours(30);

        SubjectOutGoingDto actualResult = subjectService.save(wantToSaveSubjectIncomingDto);

        SubjectOutGoingDto expectedResult = new SubjectOutGoingDto();
        expectedResult.setId(1);
        expectedResult.setName("testName");
        expectedResult.setValueOfHours(30);

        assertAll(
                () -> assertEquals(expectedResult.getId(), actualResult.getId()),
                () -> assertEquals(expectedResult.getName(), actualResult.getName()),
                () -> assertEquals(expectedResult.getValueOfHours(), actualResult.getValueOfHours())
        );
    }

    @Test
    void testUpdate() {
        Subject updateSubject = new Subject(1, "newTestName", 1000);

        Mockito.doReturn(true).when(mockitoSubjectRepository).update(updateSubject);

        SubjectIncomingDto updateSubjectIncomingDto = new SubjectIncomingDto();
        updateSubjectIncomingDto.setId(updateSubject.getId());
        updateSubjectIncomingDto.setName(updateSubject.getName());
        updateSubjectIncomingDto.setValueOfHours(updateSubject.getValueOfHours());

        assertTrue(subjectService.update(updateSubjectIncomingDto));
    }

    @Test
    void findByIdExists() {
        int id = 1;
        Subject receivedSubject = new Subject(1, "testName", 30);
        Set<Professor> professors = new LinkedHashSet<>();
        professors.add(new Professor(1, "testName", "testSurname", new Department(1, "test")));

        receivedSubject.setProfessors(professors);

        Optional<Subject> receivedSubjectOptional = Optional.of(receivedSubject);
        Mockito.doReturn(receivedSubjectOptional).when(mockitoSubjectRepository).findById(id);

        SubjectWithProfessorsOutGoingDto actualResult = subjectService.findById(id);

        SubjectWithProfessorsOutGoingDto expectedResult = new SubjectWithProfessorsOutGoingDto();
        expectedResult.setId(id);
        expectedResult.setName("testName");
        expectedResult.setValueOfHours(30);

        assertAll(
                () -> assertEquals(expectedResult.getId(), actualResult.getId()),
                () -> assertEquals(expectedResult.getName(), actualResult.getName()),
                () -> assertEquals(expectedResult.getValueOfHours(), actualResult.getValueOfHours())
        );
    }

    @Test
    void findByIdNotExists() {
        int id = 1;
        Optional<Subject> subjectOptional = Optional.empty();
        Mockito.doReturn(subjectOptional).when(mockitoSubjectRepository).findById(id);
        assertThrows(NotFoundException.class, () -> subjectService.findById(id));
    }

    @Test
    void findAll() {

        Set<Subject> receivedSubjects = new LinkedHashSet<>();
        receivedSubjects.add(new Subject(1, "testName1", 30));
        receivedSubjects.add(new Subject(2, "testName2", 30));
        receivedSubjects.add(new Subject(3, "testName3", 30));
        Mockito.doReturn(receivedSubjects).when(mockitoSubjectRepository).findAll();

        Set<SubjectOutGoingDto> actualResult = subjectService.findAll();

        Set<SubjectOutGoingDto> expectedResult = new LinkedHashSet<>();
        for (Subject receivedSubject : receivedSubjects) {
            expectedResult.add(subjectDtoMapper.map(receivedSubject));
        }

        assertEquals(expectedResult.size(), actualResult.size());
    }

    @Test
    void deleteByIdDepartmentExists() {
        int id = 1;
        Mockito.doReturn(true).when(mockitoSubjectRepository).deleteById(id);

        boolean actualResult = subjectService.deleteById(id);
        boolean expectedResult = true;

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void deleteByIdDepartmentNotExists() {
        int id = 1;
        Mockito.doReturn(false).when(mockitoSubjectRepository).deleteById(id);
        assertThrows(NotFoundException.class, () -> subjectService.deleteById(id));

    }
}