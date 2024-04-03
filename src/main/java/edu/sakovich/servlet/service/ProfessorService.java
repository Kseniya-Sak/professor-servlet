package edu.sakovich.servlet.service;

import edu.sakovich.servlet.servlet.dto.ProfessorWithSubjectsIncomingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorWithSubjectsOutGoingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorOutGoingDto;

import java.util.Set;

public interface ProfessorService {
    ProfessorWithSubjectsOutGoingDto save(ProfessorWithSubjectsIncomingDto professorIncomingDto);

    boolean update(ProfessorWithSubjectsIncomingDto professorIncomingDto);

    ProfessorWithSubjectsOutGoingDto findById(Integer id);

    Set<ProfessorOutGoingDto> findAll();

    boolean deleteById(Integer id);
}
