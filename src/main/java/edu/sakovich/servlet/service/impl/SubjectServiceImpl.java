package edu.sakovich.servlet.service.impl;

import edu.sakovich.servlet.exception.NotFoundException;
import edu.sakovich.servlet.model.Subject;
import edu.sakovich.servlet.repository.SubjectRepository;
import edu.sakovich.servlet.service.SubjectService;
import edu.sakovich.servlet.servlet.dto.SubjectIncomingDto;
import edu.sakovich.servlet.servlet.dto.SubjectOutGoingDto;
import edu.sakovich.servlet.servlet.dto.SubjectWithProfessorsOutGoingDto;
import edu.sakovich.servlet.servlet.mapper.SubjectDtoMapper;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository repository;
    private final SubjectDtoMapper mapper;

    public SubjectServiceImpl(SubjectRepository repository, SubjectDtoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public SubjectOutGoingDto save(SubjectIncomingDto subjectIncomingDto) {
        Subject incomingSubject = mapper.map(subjectIncomingDto);
        Subject savedSubject = repository.save(incomingSubject);
        return mapper.map(savedSubject);
    }

    @Override
    public boolean update(SubjectIncomingDto subjectIncomingDto) {
        boolean result = repository.update(mapper.map(subjectIncomingDto));
        if (!result) {
            throw new NotFoundException("Subject with ID = " + subjectIncomingDto.getId() +
                    " doesn't exist");
        }
        return true;
    }

    @Override
    public SubjectWithProfessorsOutGoingDto findById(Integer id) {
        Optional<Subject> subjectOptional = repository.findById(id);
        if (subjectOptional.isEmpty()) {
            throw new NotFoundException("Subject with ID = " + id + " does not exist in the database");
        }

        return mapper.mapToWithProfessorsOutGoingDto(subjectOptional.get());
    }

    @Override
    public Set<SubjectOutGoingDto> findAll() {
        Set<Subject> subjects = repository.findAll();
        Set<SubjectOutGoingDto> subjectOutGoingDtos = new LinkedHashSet<>();
        for (Subject subject : subjects) {
            subjectOutGoingDtos.add(mapper.map(subject));
        }
        return subjectOutGoingDtos;
    }

    @Override
    public boolean deleteById(Integer id) {
        boolean result = repository.deleteById(id);
        if (!result) {
            throw new NotFoundException("Subject with ID = " + id +
                    " doesn't exist");
        }
        return true;
    }
}
