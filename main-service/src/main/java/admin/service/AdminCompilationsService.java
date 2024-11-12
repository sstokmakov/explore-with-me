package admin.service;

import dto.complation.CompilationDto;
import dto.complation.NewCompilationDto;
import dto.complation.UpdateCompilationRequest;

public interface AdminCompilationsService {
    CompilationDto saveCompilations(NewCompilationDto newCompilationDto);

    void deleteCompilations(long compId);

    CompilationDto updateCompilations(long compId, UpdateCompilationRequest newCompilationDto);
}