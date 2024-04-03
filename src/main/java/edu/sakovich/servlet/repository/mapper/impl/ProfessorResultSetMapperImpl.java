package edu.sakovich.servlet.repository.mapper.impl;

import edu.sakovich.servlet.exception.ResultSetMapperException;
import edu.sakovich.servlet.model.Department;
import edu.sakovich.servlet.model.Professor;
import edu.sakovich.servlet.model.Subject;
import edu.sakovich.servlet.repository.mapper.ProfessorResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class ProfessorResultSetMapperImpl implements ProfessorResultSetMapper {
    @Override
    public Professor map(ResultSet resultSet, Department department) {
        try {
            int id = resultSet.getInt("p_id");
            String name = resultSet.getString("p_name");
            String surname = resultSet.getString("surname");
            return new Professor(id, name, surname, department);
        } catch (SQLException e) {
            throw new ResultSetMapperException(e);
        }
    }

    @Override
    public Professor map(ResultSet resultSet, Professor professor) {
        try {
            int id = resultSet.getInt("p_id");
            String name = professor.getName();
            String surname = professor.getSurname();
            return new Professor(id, name, surname, professor.getDepartment());
        } catch (SQLException e) {
            throw new ResultSetMapperException(e);
        }
    }

    @Override
    public Professor mapSingleResultWithSetSubjects(int id, Professor professor) {
        return new Professor(id, professor.getName(), professor.getSurname(),
                professor.getDepartment(), professor.getSubjects());
    }

    @Override
    public Optional<Professor> mapSingleResultWithSetSubjects(ResultSet resultSet) {
        try {
            Professor professor = null;
            Set<Subject> subjects = new LinkedHashSet<>();
            int profes = 0;
            while (resultSet.next()) {
                if (profes++ == 0) {
                    Department department = new Department();
                    department.setId(resultSet.getInt("d_id"));
                    department.setName(resultSet.getString("d_name"));
                    professor = map(resultSet, department);
                }
                Subject subject = new Subject();
                subject.setId(resultSet.getInt("s_id"));
                subject.setName(resultSet.getString("s_name"));
                subject.setValueOfHours(resultSet.getInt("value_of_hours"));
                subjects.add(subject);
            }
            if (professor == null) {
                return Optional.empty();
            }
            professor.setSubjects(subjects);
            return Optional.of(professor);
        } catch (SQLException e) {
            throw new ResultSetMapperException(e);
        }
    }
}
