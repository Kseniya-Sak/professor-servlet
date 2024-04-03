package edu.sakovich.servlet.repository.impl;

import edu.sakovich.servlet.db.ConnectionManager;
import edu.sakovich.servlet.exception.RepositoryException;
import edu.sakovich.servlet.model.Department;
import edu.sakovich.servlet.model.Professor;
import edu.sakovich.servlet.model.Subject;
import edu.sakovich.servlet.repository.DepartmentRepository;
import edu.sakovich.servlet.repository.ProfessorRepository;
import edu.sakovich.servlet.repository.SubjectRepository;
import edu.sakovich.servlet.repository.mapper.ProfessorResultSetMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static edu.sakovich.servlet.repository.ParameterHandler.departmentNotExist;
import static edu.sakovich.servlet.repository.ParameterHandler.professorNotExist;
import static edu.sakovich.servlet.repository.ParameterHandler.subjectNotExist;
import static edu.sakovich.servlet.repository.ParameterHandler.throwExceptionIfParameterNull;

public class ProfessorRepositoryImpl implements ProfessorRepository {
    private final ConnectionManager connectionManager;
    private final ProfessorResultSetMapper mapper;
    private final DepartmentRepository departmentRepository;
    private final SubjectRepository subjectRepository;

    public ProfessorRepositoryImpl(ConnectionManager connectionManager, ProfessorResultSetMapper mapper, DepartmentRepository departmentRepository, SubjectRepository subjectRepository) {
        this.connectionManager = connectionManager;
        this.mapper = mapper;
        this.departmentRepository = departmentRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public Professor save(Professor professor) {
        checkAllParameters(professor);

        if (exitsByNameAndSurname(professor.getName(), professor.getSurname())) {
            throw new RepositoryException("""
                    A professor with name "%s" and surname "%s" already exists
                    """.formatted(professor.getName(), professor.getSurname()));
        }

        throwExceptionIfParameterNull(departmentRepository.findById(professor.getDepartment().getId()).isEmpty(),
                departmentNotExist(professor.getDepartment().getId()));

        for (Subject subject : professor.getSubjects()) {
            throwExceptionIfParameterNull(subjectRepository.findById(subject.getId()).isEmpty(),
                    subjectNotExist(subject.getId()));
        }


        String saveProfessor = "insert into professor(p_name, surname, department_id) values (?, ?, ?);";
        String saveProfessorSubjects = "insert into professor_subject(professor_id, subject_id) values (?, ?);";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement1 = connection
                     .prepareStatement(saveProfessor, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement preparedStatement2 = connection
                     .prepareStatement(saveProfessorSubjects)) {
            preparedStatement1.setString(1, professor.getName());
            preparedStatement1.setString(2, professor.getSurname());
            preparedStatement1.setInt(3, professor.getDepartment().getId());

            preparedStatement1.executeUpdate();

            ResultSet generatedKeys = preparedStatement1.getGeneratedKeys();
            generatedKeys.next();
            int id = generatedKeys.getInt("p_id");
            for (Subject subject : professor.getSubjects()) {
                preparedStatement2.setInt(1, id);
                preparedStatement2.setInt(2, subject.getId());
                preparedStatement2.executeUpdate();
            }

            return mapper.mapSingleResultWithSetSubjects(id, professor);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private void checkAllParameters(Professor professor) {
        throwExceptionIfParameterNull(professor,"The professor must not be null");
        throwExceptionIfParameterNull(professor.getName(),"The professor's name must not be null");
        throwExceptionIfParameterNull(professor.getSurname(),"The professor's surname must not be null");
        throwExceptionIfParameterNull(professor.getDepartment(),"The professor's department must not be null");
    }

    @Override
    public Set<Professor> findAll() {
        Set<Professor> professors = new LinkedHashSet<>();
        String findAllSql = "select * from professor order by p_id;";
        try (Connection connection = connectionManager.getConnection();
        Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(findAllSql);

            while (resultSet.next()) {
                Optional<Department> optionalDepartment = departmentRepository.findById(resultSet.getInt("department_id"));
                professors.add(mapper.map(resultSet, optionalDepartment.orElse(new Department())));
            }
            return professors;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Professor> findById(Integer id) {
        throwExceptionIfParameterNull(id, "The ID must not be null");

        String findById = """
                Select p_id, p_name, surname, d_id, d_name, s_id, s_name, value_of_hours from professor
                    inner join department d on d.d_id = professor.department_id
                    inner join professor_subject ps on professor.p_id = ps.professor_id
                    inner join subject s on s.s_id = ps.subject_id where p_id = ?;
                """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findById)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapper.mapSingleResultWithSetSubjects(resultSet);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean update(Professor professor) {
        checkAllParameters(professor);

        throwExceptionIfParameterNull(findById(professor.getId()).isEmpty(),
                professorNotExist(professor.getId()));

        throwExceptionIfParameterNull(departmentRepository.findById(professor.getDepartment().getId()).isEmpty(),
                professorNotExist(professor.getDepartment().getId()));

        for (Subject subject : professor.getSubjects()) {
            throwExceptionIfParameterNull(subjectRepository.findById(subject.getId()).isEmpty(),
                    subjectNotExist(subject.getId()));
        }

        deleteFromProfessorSubjectById(professor.getId());

        String updateSql = "UPDATE professor SET p_name = ?, surname = ?, department_id = ? WHERE p_id = ?;";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {

            preparedStatement.setString(1, professor.getName());
            preparedStatement.setString(2, professor.getSurname());
            preparedStatement.setInt(3, professor.getDepartment().getId());
            preparedStatement.setInt(4, professor.getId());

            int result = preparedStatement.executeUpdate();

            for (Subject subject : professor.getSubjects()) {
                saveToProfessorSubjectById(professor.getId(), subject.getId());
            }

            return result > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        throwExceptionIfParameterNull(id, "The ID must not be null");

        deleteFromProfessorSubjectById(id);
        String deleteProfessor = "delete from professor where p_id = ?;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteProfessor);) {
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private void deleteFromProfessorSubjectById(int id) {
        String deleteProfessorSubject = """
                delete from professor_subject where "professor_id" = ?
                """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteProfessorSubject)) {
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private void saveToProfessorSubjectById(int professor_id, int subject_id) {
        String saveProfessorSubjects = "insert into professor_subject(professor_id, subject_id) values (?, ?);";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(saveProfessorSubjects)) {
            preparedStatement.setInt(1, professor_id);
            preparedStatement.setInt(2, subject_id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private boolean exitsByNameAndSurname(String name, String surname) {
        String checkSql = "select exists (select p_id from professor where p_name = ? and surname = ?);";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(checkSql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getBoolean(1);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

}
