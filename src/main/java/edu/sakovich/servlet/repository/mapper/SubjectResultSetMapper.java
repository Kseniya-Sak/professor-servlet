package edu.sakovich.servlet.repository.mapper;

import edu.sakovich.servlet.model.Subject;

import java.sql.ResultSet;
import java.util.Optional;

public interface SubjectResultSetMapper {
    Subject map(ResultSet resultSet);
    Subject map(ResultSet resultSet, Subject subject);

    Optional<Subject> mapSingleResultWithSetProfessors(ResultSet resultSet);
}
