package admin.service;

import dto.complation.CompilationDto;
import dto.complation.NewCompilationDto;
import dto.complation.UpdateCompilationRequest;
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
