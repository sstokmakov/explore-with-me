package ru.tokmakov.exception;

public class OperationPreconditionFailedException extends RuntimeException {
    public OperationPreconditionFailedException(String message) {
        super(message);
    }
}
