package ru.tokmakov.exception.event;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
