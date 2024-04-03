package edu.sakovich.servlet.service.impl;

import edu.sakovich.servlet.exception.NotFoundException;
import edu.sakovich.servlet.model.Professor;
import edu.sakovich.servlet.repository.ProfessorRepository;
import edu.sakovich.servlet.service.ProfessorService;
import edu.sakovich.servlet.servlet.dto.ProfessorWithSubjectsIncomingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorWithSubjectsOutGoingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorOutGoingDto;
import edu.sakovich.servlet.servlet.mapper.ProfessorDtoMapper;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class ProfessorServiceImpl implements ProfessorService {
    private final ProfessorRepository repository;
    private final ProfessorDtoMapper mapper;

    public ProfessorServiceImpl(ProfessorRepository repository, ProfessorDtoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ProfessorWithSubjectsOutGoingDto save(ProfessorWithSubjectsIncomingDto professorIncomingDto) {
        Professor incomingProfessor = mapper.mapProfessorWithSubjectsOutGoingDto(professorIncomingDto);
        Professor savedProfessor = repository.save(incomingProfessor);
        return mapper.mapProfessorWithSubjectsOutGoingDto(savedProfessor);
    }

    @Override
    public boolean update(ProfessorWithSubjectsIncomingDto professorIncomingDto) {
        return repository.update(mapper.mapProfessorWithSubjectsOutGoingDto(professorIncomingDto));
    }

    @Override
    public ProfessorWithSubjectsOutGoingDto findById(Integer id) {
        Optional<Professor> professorOptional = repository.findById(id);
        if (professorOptional.isEmpty()) {
            throw new NotFoundException("Professor with ID = " + id + " does not exist in the database");
        }
        return mapper.mapProfessorWithSubjectsOutGoingDto(professorOptional.get());
    }

    @Override
    public Set<ProfessorOutGoingDto> findAll() {
        Set<Professor> professors = repository.findAll();
        Set<ProfessorOutGoingDto> professorOutGoingDtos = new LinkedHashSet<>();
        for (Professor professor : professors) {
            professorOutGoingDtos.add(mapper.map(professor));
        }
        return professorOutGoingDtos;
    }

    @Override
    public boolean deleteById(Integer id) {
        boolean result = repository.deleteById(id);
        if (!result) {
            throw new NotFoundException("Professor with ID = " + id +
                    " doesn't exist");
        }
        return true;

    }
}
