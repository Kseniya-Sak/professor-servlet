package edu.sakovich.servlet.exception;

public class DataBaseDriverLoadException extends RuntimeException {
    public DataBaseDriverLoadException(String message) {
        super(message);
    }
}
