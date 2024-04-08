package edu.sakovich.servlet.repository;

import edu.sakovich.servlet.exception.RepositoryException;

import java.util.Objects;

public class ParameterHandler {

    private ParameterHandler() {}

    public static void throwExceptionIfParameterNull(Object parameter, String message) {
        if (Objects.isNull(parameter)) {
            throw new RepositoryException(message);
        }
    }

    public static void throwExceptionIfParameterNull(boolean check, String message) {
        if (check) {
            throw new RepositoryException(message);
        }
    }

    public static void throwExceptionIfParameterNull(int parameter, String message) {
        if (parameter <= 0) {
            throw new RepositoryException(message);
        }
    }

    public static String departmentNotExist(int departmentId) {
       return  """
            Department with ID = %d doesn't exist.
            Add such a department in the database
            """.formatted(departmentId);
    }

    public static String subjectNotExist(int subjectId) {
        return  """
            Subject with ID = %d doesn't exist.
            Add such a subject in the database
            """.formatted(subjectId);
    }

    public static String professorNotExist(int professorId) {
        return  """
            Professor with ID = %d doesn't exist.
            Add such a professor in the database
            """.formatted(professorId);
    }

}
