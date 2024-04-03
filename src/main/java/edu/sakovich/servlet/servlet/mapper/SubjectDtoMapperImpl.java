package edu.sakovich.servlet.servlet.mapper;

import edu.sakovich.servlet.model.Professor;
import edu.sakovich.servlet.model.Subject;
import edu.sakovich.servlet.servlet.dto.ProfessorOutGoingDto;
import edu.sakovich.servlet.servlet.dto.SubjectIncomingDto;
import edu.sakovich.servlet.servlet.dto.SubjectOutGoingDto;
import edu.sakovich.servlet.servlet.dto.SubjectWithProfessorsOutGoingDto;

import java.util.LinkedHashSet;
import java.util.Set;

public class SubjectDtoMapperImpl implements SubjectDtoMapper {
    ProfessorDtoMapper professorDtoMapper = new ProfessorDtoMapperImpl();

    @Override
    public Subject map(SubjectIncomingDto incomingDto) {
        Subject subject = new Subject();
        subject.setId(incomingDto.getId());
        subject.setName(incomingDto.getName());
        subject.setValueOfHours(incomingDto.getValueOfHours());
        return subject;
    }

    @Override
    public SubjectOutGoingDto map(Subject subject) {
        SubjectOutGoingDto subjectOutGoingDto = new SubjectOutGoingDto();
        subjectOutGoingDto.setId(subject.getId());
        subjectOutGoingDto.setName(subject.getName());
        subjectOutGoingDto.setValueOfHours(subject.getValueOfHours());
        return subjectOutGoingDto;
    }

    @Override
    public SubjectWithProfessorsOutGoingDto mapToWithProfessorsOutGoingDto(Subject subject) {
        SubjectWithProfessorsOutGoingDto subjectOutGoingDto = new SubjectWithProfessorsOutGoingDto();
        subjectOutGoingDto.setId(subject.getId());
        subjectOutGoingDto.setName(subject.getName());
        subjectOutGoingDto.setValueOfHours(subject.getValueOfHours());

        Set<ProfessorOutGoingDto> professorOutGoingDtos = new LinkedHashSet<>();
        for (Professor professor : subject.getProfessors()) {
            professorOutGoingDtos.add(professorDtoMapper.map(professor));
        }

        subjectOutGoingDto.setProfessors(professorOutGoingDtos);
        return subjectOutGoingDto;
    }

}
