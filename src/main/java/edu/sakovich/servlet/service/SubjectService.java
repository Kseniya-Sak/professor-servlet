package edu.sakovich.servlet.service;

import edu.sakovich.servlet.servlet.dto.SubjectIncomingDto;
import edu.sakovich.servlet.servlet.dto.SubjectOutGoingDto;
import edu.sakovich.servlet.servlet.dto.SubjectWithProfessorsOutGoingDto;

import java.util.Set;

public interface SubjectService {
    SubjectOutGoingDto save(SubjectIncomingDto subjectIncomingDto);

    boolean update(SubjectIncomingDto subjectIncomingDto);

    SubjectWithProfessorsOutGoingDto findById(Integer id);

    Set<SubjectOutGoingDto> findAll();

    boolean deleteById(Integer id);
}
