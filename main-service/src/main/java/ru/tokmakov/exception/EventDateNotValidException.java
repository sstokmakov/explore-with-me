package ru.tokmakov.exception;

public class EventDateNotValidException extends RuntimeException {
  public EventDateNotValidException(String message) {
    super(message);
  }
}
