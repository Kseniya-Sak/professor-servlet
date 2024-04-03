package edu.sakovich.servlet.servlet.mapper;

import edu.sakovich.servlet.model.Department;
import edu.sakovich.servlet.model.Professor;
import edu.sakovich.servlet.model.Subject;
import edu.sakovich.servlet.servlet.dto.DepartmentIncomingDto;
import edu.sakovich.servlet.servlet.dto.DepartmentOutGoingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorWithSubjectsIncomingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorWithSubjectsOutGoingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorIncomingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorOutGoingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorShortOutGoingDto;
import edu.sakovich.servlet.servlet.dto.SubjectIncomingDto;
import edu.sakovich.servlet.servlet.dto.SubjectOutGoingDto;

import java.util.LinkedHashSet;
import java.util.Set;

public class ProfessorDtoMapperImpl implements ProfessorDtoMapper {
    private static final DepartmentDtoMapper departmentDtoMapper = new DepartmentDtoMapperImpl();
    private static final SubjectDtoMapper subjectDtoMapper = new SubjectDtoMapperImpl();

    @Override
    public Professor map(ProfessorIncomingDto incomingDto) {
        Professor professor = new Professor();
        professor.setId(incomingDto.getId());
        professor.setName(incomingDto.getName());
        professor.setSurname(incomingDto.getSurname());

        DepartmentIncomingDto departmentIncomingDto = incomingDto.getDepartmentIncomingDto();
        Department department = departmentDtoMapper.map(departmentIncomingDto);

        professor.setDepartment(department);
        return professor;
    }

    @Override
    public ProfessorOutGoingDto map(Professor professor) {
        ProfessorOutGoingDto outGoingDto = new ProfessorOutGoingDto();
        outGoingDto.setId(professor.getId());
        outGoingDto.setName(professor.getName());
        outGoingDto.setSurname(professor.getSurname());

        Department department = professor.getDepartment();
        DepartmentOutGoingDto departmentOutGoingDto = departmentDtoMapper.map(department);

        outGoingDto.setDepartment(departmentOutGoingDto);
        return outGoingDto;
    }

    @Override
    public ProfessorShortOutGoingDto mapToProfessorShortOutGoingDto(Professor professor) {
        ProfessorShortOutGoingDto outGoingDto = new ProfessorShortOutGoingDto();
        outGoingDto.setId(professor.getId());
        outGoingDto.setName(professor.getName());
        outGoingDto.setSurname(professor.getSurname());
        return outGoingDto;
    }

    @Override
    public ProfessorWithSubjectsOutGoingDto mapProfessorWithSubjectsOutGoingDto(Professor professor) {
        ProfessorWithSubjectsOutGoingDto outGoingDto = new ProfessorWithSubjectsOutGoingDto();
        outGoingDto.setId(professor.getId());
        outGoingDto.setName(professor.getName());
        outGoingDto.setSurname(professor.getSurname());

        Department department = professor.getDepartment();
        DepartmentOutGoingDto departmentOutGoingDto = departmentDtoMapper.map(department);

        outGoingDto.setDepartment(departmentOutGoingDto);

        Set<Subject> subjects = professor.getSubjects();
        Set<SubjectOutGoingDto> subjectOutGoingDtos = new LinkedHashSet<>();
        for (Subject subject : subjects) {
            subjectOutGoingDtos.add(subjectDtoMapper.map(subject));
        }
        outGoingDto.setSubjects(subjectOutGoingDtos);
        return outGoingDto;
    }

    @Override
    public Professor mapProfessorWithSubjectsOutGoingDto(ProfessorWithSubjectsIncomingDto incomingDto) {
        Professor professor = new Professor();
        professor.setId(incomingDto.getId());
        professor.setName(incomingDto.getName());
        professor.setSurname(incomingDto.getSurname());

        DepartmentIncomingDto departmentIncomingDto = incomingDto.getDepartment();
        Department department = departmentDtoMapper.map(departmentIncomingDto);

        professor.setDepartment(department);

        Set<SubjectIncomingDto> subjectIncomingDtos = incomingDto.getSubjects();
        Set<Subject> subjects = new LinkedHashSet<>();
        for (SubjectIncomingDto subject : subjectIncomingDtos) {
           subjects.add(subjectDtoMapper.map(subject));
        }
        professor.setSubjects(subjects);
        return professor;
    }
}
