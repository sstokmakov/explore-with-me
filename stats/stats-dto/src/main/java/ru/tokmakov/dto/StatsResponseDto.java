package ru.tokmakov.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatsResponseDto {
    private String app;
    private String uri;
    private long hits;
}