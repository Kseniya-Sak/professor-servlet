package edu.sakovich.servlet.repository.mapper;

import edu.sakovich.servlet.model.Department;

import java.sql.ResultSet;
import java.util.Optional;

public interface DepartmentResultSetMapper {
    Department map(ResultSet resultSet);
    Department map(ResultSet resultSet, Department department);

    Optional<Department> mapSingleResultWithSetProfessors(ResultSet resultSet);
}
