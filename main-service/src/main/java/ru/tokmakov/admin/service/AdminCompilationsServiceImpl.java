package ru.tokmakov.admin.service;

import ru.tokmakov.dto.complation.CompilationDto;
import ru.tokmakov.dto.complation.NewCompilationDto;
import ru.tokmakov.dto.complation.UpdateCompilationRequest;
import org.springframework.stereotype.Service;

@Service
public class AdminCompilationsServiceImpl implements AdminCompilationsService {
    @Override
    public CompilationDto saveCompilations(NewCompilationDto newCompilationDto) {
        return null;
    }

    @Override
    public void deleteCompilations(long compId) {

    }

    @Override
    public CompilationDto updateCompilations(long compId, UpdateCompilationRequest newCompilationDto) {
        return null;
    }
}
