package edu.sakovich.servlet.servlet.mapper;

import edu.sakovich.servlet.model.Subject;
import edu.sakovich.servlet.servlet.dto.SubjectIncomingDto;
import edu.sakovich.servlet.servlet.dto.SubjectOutGoingDto;
import edu.sakovich.servlet.servlet.dto.SubjectWithProfessorsOutGoingDto;

public interface SubjectDtoMapper {
    Subject map(SubjectIncomingDto incomingDto);
    SubjectOutGoingDto map(Subject subject);

    SubjectWithProfessorsOutGoingDto mapToWithProfessorsOutGoingDto(Subject subject);
}
