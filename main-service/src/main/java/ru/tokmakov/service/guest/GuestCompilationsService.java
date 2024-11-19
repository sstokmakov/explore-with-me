package ru.tokmakov.service.guest;

import ru.tokmakov.dto.complation.CompilationDto;

import java.util.List;

public interface GuestCompilationsService {
    List<CompilationDto> findCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto findCompilationsByCompId(Long compId);
}