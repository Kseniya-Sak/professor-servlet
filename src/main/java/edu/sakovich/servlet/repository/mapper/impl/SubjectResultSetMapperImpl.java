package edu.sakovich.servlet.repository.mapper.impl;

import edu.sakovich.servlet.exception.ResultSetMapperException;
import edu.sakovich.servlet.model.Department;
import edu.sakovich.servlet.model.Professor;
import edu.sakovich.servlet.model.Subject;
import edu.sakovich.servlet.repository.mapper.SubjectResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class SubjectResultSetMapperImpl implements SubjectResultSetMapper {
    @Override
    public Subject map(ResultSet resultSet) {
        try {
            int id = resultSet.getInt("s_id");
            String name = resultSet.getString("s_name");
            int valueOfHours = resultSet.getInt("value_of_hours");
            return new Subject(id, name, valueOfHours);
        } catch (SQLException e) {
            throw new ResultSetMapperException(e);
        }
    }

    @Override
    public Subject map(ResultSet resultSet, Subject subject) {
        try {
            int id = resultSet.getInt("s_id");
            String name = subject.getName();
            int valueOfHours = subject.getValueOfHours();
            return new Subject(id, name, valueOfHours);
        } catch (SQLException e) {
            throw new ResultSetMapperException(e);
        }
    }

    @Override
    public Optional<Subject> mapSingleResultWithSetProfessors(ResultSet resultSet) {
        try {
            Subject subject = null;
            Set<Professor> professors = new LinkedHashSet<>();
            int subj = 0;
            while (resultSet.next()) {
                if (subj++ == 0) {
                    subject = map(resultSet);
                }

                Professor professor = new Professor();
                professor.setId(resultSet.getInt("p_id"));
                professor.setName(resultSet.getString("p_name"));
                professor.setSurname((resultSet.getString("surname")));

                Department department = new Department();
                department.setId(resultSet.getInt("d_id"));
                department.setName(resultSet.getString("d_name"));

                professor.setDepartment(department);

                professors.add(professor);
            }

            if (subject == null) {
                return Optional.empty();
            }
            subject.setProfessors(professors);
            return Optional.of(subject);
        } catch (SQLException e) {
            throw new ResultSetMapperException(e);
        }
    }


}
