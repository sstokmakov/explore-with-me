package ru.tokmakov.service;

import ru.tokmakov.dto.HitDto;
import ru.tokmakov.dto.StatsResponseDto;

import java.util.List;

public interface StatsService {
    HitDto save(HitDto hitDto);

    List<StatsResponseDto> getStats(String start, String end, List<String> uris, boolean unique);
}
