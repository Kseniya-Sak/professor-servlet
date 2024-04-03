package edu.sakovich.servlet.repository.impl;

import edu.sakovich.servlet.db.ConnectionManager;
import edu.sakovich.servlet.exception.RepositoryException;
import edu.sakovich.servlet.model.Subject;
import edu.sakovich.servlet.repository.SubjectRepository;
import edu.sakovich.servlet.repository.mapper.SubjectResultSetMapper;

import javax.crypto.spec.PSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static edu.sakovich.servlet.repository.ParameterHandler.throwExceptionIfParameterNull;

public class SubjectRepositoryImpl implements SubjectRepository {
    private final ConnectionManager connectionManager;
    private final SubjectResultSetMapper mapper;

    public SubjectRepositoryImpl(ConnectionManager connectionManager, SubjectResultSetMapper mapper) {
        this.connectionManager = connectionManager;
        this.mapper = mapper;
    }

    @Override
    public Subject save(Subject subject) {
        throwExceptionIfParameterNull(subject, "The subject must not be null");
        throwExceptionIfParameterNull(subject.getName(), "The subject's name must not be null");
        throwExceptionIfParameterNull(subject.getValueOfHours(), "The subject's value of hours must be greater than 0");

        if (exitsByName(subject.getName())) {
            throw new RepositoryException("""
                    A subject named "%s" already exists
                    """.formatted(subject.getName()));
        }

        String saveQuery = "insert into subject(s_name, value_of_hours) values (?, ?);";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(saveQuery, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, subject.getName());
            statement.setInt(2, subject.getValueOfHours());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            return mapper.map(resultSet, subject);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Set<Subject> findAll() {
        Set<Subject> subjects = new LinkedHashSet<>();
        String findAllSql = "select * from subject order by s_id;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findAllSql)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                subjects.add(mapper.map(resultSet));
            }
            return subjects;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Subject> findById(Integer id) {
        throwExceptionIfParameterNull(id, "The ID must not be null");

        String findById = """
                select s_id, s_name, value_of_hours, p_id, p_name, surname, d_id, d_name from subject\s
                    join professor_subject ps on subject.s_id = ps.subject_id
                    join professor p on p.p_id = ps.professor_id
                    join department d on d.d_id = p.department_id
                where s_id = ?;
                """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findById)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapper.mapSingleResultWithSetProfessors(resultSet);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }



    @Override
    public boolean update(Subject subject) {
        throwExceptionIfParameterNull(subject, "The subject must not be null");
        throwExceptionIfParameterNull(subject.getName(), "The subject's name must not be null");
        throwExceptionIfParameterNull(subject.getValueOfHours(), "The subject's value of hours must be greater than 0");

        String updateSql = "UPDATE subject SET s_name = ?, value_of_hours = ? WHERE s_id = ?;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSql);) {

            preparedStatement.setString(1, subject.getName());
            preparedStatement.setInt(2, subject.getValueOfHours());
            preparedStatement.setInt(3, subject.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        throwExceptionIfParameterNull(id, "The ID must not be null");

        deleteFromProfessorSubjectById(id);

        String deleteSql = "delete from subject where s_id = ?;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSql);) {
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private void deleteFromProfessorSubjectById(int subjectId) {
        String deleteSql = "delete from professor_subject where subject_id = ?;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSql);) {
            System.out.println(connection.isClosed());

            preparedStatement.setInt(1, subjectId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private boolean exitsByName(String name) {
        String checkSql = "SELECT EXISTS (SELECT s_id FROM subject WHERE s_name = '" + name + "')";
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(checkSql);
            resultSet.next();
            return resultSet.getBoolean(1);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

}
