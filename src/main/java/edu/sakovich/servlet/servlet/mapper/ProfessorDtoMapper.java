package edu.sakovich.servlet.servlet.mapper;

import edu.sakovich.servlet.model.Professor;
import edu.sakovich.servlet.servlet.dto.ProfessorWithSubjectsIncomingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorWithSubjectsOutGoingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorIncomingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorOutGoingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorShortOutGoingDto;

public interface ProfessorDtoMapper {
    Professor map(ProfessorIncomingDto incomingDto);

    ProfessorOutGoingDto map(Professor professor);

    ProfessorShortOutGoingDto mapToProfessorShortOutGoingDto(Professor professor);

    ProfessorWithSubjectsOutGoingDto mapProfessorWithSubjectsOutGoingDto(Professor professor);

    Professor mapProfessorWithSubjectsOutGoingDto(ProfessorWithSubjectsIncomingDto incomingDto);
}
