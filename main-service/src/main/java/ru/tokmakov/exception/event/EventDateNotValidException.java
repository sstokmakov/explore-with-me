package ru.tokmakov.exception.event;

public class EventDateNotValidException extends RuntimeException {
  public EventDateNotValidException(String message) {
    super(message);
  }
}
