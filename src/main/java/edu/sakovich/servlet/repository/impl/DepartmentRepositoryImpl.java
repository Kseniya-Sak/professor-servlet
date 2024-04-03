package edu.sakovich.servlet.repository.impl;

import edu.sakovich.servlet.db.ConnectionManager;
import edu.sakovich.servlet.exception.RepositoryException;
import edu.sakovich.servlet.model.Department;
import edu.sakovich.servlet.repository.DepartmentRepository;
import edu.sakovich.servlet.repository.mapper.DepartmentResultSetMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static edu.sakovich.servlet.repository.ParameterHandler.throwExceptionIfParameterNull;

public class DepartmentRepositoryImpl implements DepartmentRepository {
    private final ConnectionManager connectionManager;
    private final DepartmentResultSetMapper departmentResultSetMapper;

    public DepartmentRepositoryImpl(ConnectionManager connectionManager, DepartmentResultSetMapper departmentResultSetMapper) {
        this.connectionManager = connectionManager;
        this.departmentResultSetMapper = departmentResultSetMapper;
    }
    @Override
    public Department save(Department department) {
        throwExceptionIfParameterNull(department, "The department must not be null");
        throwExceptionIfParameterNull(department.getName(), "The department's name must not be null");

        if (exitsByName(department.getName())) {
            throw new RepositoryException("""
                    A department named "%s" already exists
                    """.formatted(department.getName()));
        }

        String saveQuery = "insert into department (d_name) values (?);";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(saveQuery, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, department.getName());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            return departmentResultSetMapper.map(resultSet, department);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Set<Department> findAll() {
        Set<Department> departments = new LinkedHashSet<>();
        String findAllSql = "select * from department order by d_id;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findAllSql)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                departments.add(departmentResultSetMapper.map(resultSet));
            }
            return departments;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Department> findById(Integer id) {
        throwExceptionIfParameterNull(id, "The ID must not be null");

        String findById = """
                select d_id, d_name, p_id, p_name, surname
                from department as d join professor p on d_id = p.department_id
                where d_id = ?
                """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findById)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return departmentResultSetMapper.mapSingleResultWithSetProfessors(resultSet);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean update(Department department) {
        throwExceptionIfParameterNull(department, "The department must not be null");
        throwExceptionIfParameterNull(department.getName(), "The department's name must not be null");

        String updateSql = "UPDATE department SET d_name = ? WHERE d_id = ?;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSql);) {

            preparedStatement.setString(1, department.getName());
            preparedStatement.setInt(2, department.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        throwExceptionIfParameterNull(id, "The ID must not be null");

        findById(id);

        String deleteFromProfessorSubject = """
                delete from professor_subject
                where professor_id in
                      (select p_id from professor
                      where department_id = ?)
                """;
        String deleteFromProfessor = "delete from professor where department_id = ?;";
        String deleteDepartment = "delete from department where d_id = ?;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement1 = connection.prepareStatement(deleteFromProfessorSubject);
             PreparedStatement preparedStatement2 = connection.prepareStatement(deleteFromProfessor);
             PreparedStatement preparedStatement3 = connection.prepareStatement(deleteDepartment) ) {

            preparedStatement1.setInt(1, id);
            preparedStatement2.setInt(1, id);
            preparedStatement3.setInt(1, id);

            preparedStatement1.executeUpdate();
            preparedStatement2.executeUpdate();
            return preparedStatement3.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private boolean exitsByName(String name) {
        String checkSql = "SELECT EXISTS (SELECT d_id FROM department WHERE d_name = '" + name + "')";
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
