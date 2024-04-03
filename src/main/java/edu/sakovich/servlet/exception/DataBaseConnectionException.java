package edu.sakovich.servlet.exception;

public class DataBaseConnectionException extends RuntimeException {
    public DataBaseConnectionException(String message) {
        super(message);
    }
}
