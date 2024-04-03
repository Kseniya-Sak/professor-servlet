package edu.sakovich.servlet.repository.mapper.impl;

import edu.sakovich.servlet.exception.ResultSetMapperException;
import edu.sakovich.servlet.model.Department;
import edu.sakovich.servlet.model.Professor;
import edu.sakovich.servlet.repository.mapper.DepartmentResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class DepartmentResultSetMapperImpl implements DepartmentResultSetMapper {

    @Override
    public Department map(ResultSet resultSet) {
        try {
            int id = resultSet.getInt("d_id");
            String name = resultSet.getString("d_name");
            return new Department(id, name);
        } catch (SQLException e) {
            throw new ResultSetMapperException(e);
        }
    }

    @Override
    public Department map(ResultSet resultSet, Department department) {
        try {
            int id = resultSet.getInt("d_id");
            String name = department.getName();
            return new Department(id, name);
        } catch (SQLException e) {
            throw new ResultSetMapperException(e);
        }
    }

    @Override
    public Optional<Department> mapSingleResultWithSetProfessors(ResultSet resultSet) {
        try {
            Department department = null;
            Set<Professor> professors = new LinkedHashSet<>();
            int depart = 0;
            while (resultSet.next()) {
                if (depart++ == 0) {
                    department = map(resultSet);
                }
                Professor professor = new Professor();
                professor.setId(resultSet.getInt("p_id"));
                professor.setName(resultSet.getString("p_name"));
                professor.setSurname((resultSet.getString("surname")));
                professors.add(professor);
            }
            if (department == null) {
                return Optional.empty();
            }
            department.setProfessors(professors);
            return Optional.of(department);
        } catch (SQLException e) {
            throw new ResultSetMapperException(e);
        }
    }


}
