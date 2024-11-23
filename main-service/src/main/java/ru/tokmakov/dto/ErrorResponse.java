package ru.tokmakov.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private String status;
    private String reason;
    private String message;
    private String timestamp;
}