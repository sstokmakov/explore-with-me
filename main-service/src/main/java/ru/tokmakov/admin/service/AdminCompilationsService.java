package ru.tokmakov.admin.service;

import ru.tokmakov.dto.complation.CompilationDto;
import ru.tokmakov.dto.complation.NewCompilationDto;
import ru.tokmakov.dto.complation.UpdateCompilationRequest;

public interface AdminCompilationsService {
    CompilationDto saveCompilations(NewCompilationDto newCompilationDto);

    void deleteCompilations(long compId);

    CompilationDto updateCompilations(long compId, UpdateCompilationRequest newCompilationDto);
}