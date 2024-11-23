package ru.tokmakov.exception;

public class TitleAlreadyExistsException extends RuntimeException {
    public TitleAlreadyExistsException(String message) {
        super(message);
    }
}
