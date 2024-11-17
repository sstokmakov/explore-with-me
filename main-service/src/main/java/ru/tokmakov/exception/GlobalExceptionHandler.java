package ru.tokmakov.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.tokmakov.dto.ErrorResponse;
import ru.tokmakov.exception.category.CategoryNameAlreadyExistsException;
import ru.tokmakov.exception.category.CategoryNotEmptyException;
import ru.tokmakov.exception.compilation.TitleAlreadyExistsException;
import ru.tokmakov.exception.event.ConflictException;
import ru.tokmakov.exception.event.EventDateException;
import ru.tokmakov.exception.event.EventDateNotValidException;
import ru.tokmakov.exception.event.EventStateException;
import ru.tokmakov.exception.user.EmailAlreadyExistsException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class, BadRequestException.class, MissingServletRequestParameterException.class})
    public ErrorResponse handleInvalidArgument(Exception e) {

        return new ErrorResponse(
                "BAD_REQUEST",
                "Incorrectly made request.",
                e.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }

    @ExceptionHandler(
            {
                    CategoryNameAlreadyExistsException.class,
                    EmailAlreadyExistsException.class,
                    EventDateNotValidException.class
            })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolationException(RuntimeException e) {
        return new ErrorResponse(
                "CONFLICT",
                "Integrity constraint has been violated.",
                e.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(RuntimeException e) {
        return new ErrorResponse(
                "NOT_FOUND",
                "The required object was not found.",
                e.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }

    @ExceptionHandler({EventStateException.class, EventDateException.class, CategoryNotEmptyException.class, ConflictException.class, TitleAlreadyExistsException.class, ForbiddenAccessException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(RuntimeException e) {
        return new ErrorResponse(
                "CONFLICT",
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }
}