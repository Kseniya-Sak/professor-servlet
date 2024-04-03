package edu.sakovich.servlet.repository.mapper;

import edu.sakovich.servlet.model.Department;
import edu.sakovich.servlet.model.Professor;

import java.sql.ResultSet;
import java.util.Optional;

public interface ProfessorResultSetMapper {
    Professor map(ResultSet resultSet, Department department);
    Professor map(ResultSet resultSet, Professor professor);

    Professor mapSingleResultWithSetSubjects(int id, Professor professor);

    Optional<Professor> mapSingleResultWithSetSubjects(ResultSet resultSet);
}
