package ru.tokmakov.guest.service;

import ru.tokmakov.dto.complation.CompilationDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestCompilationsServiceImpl implements GuestCompilationsService {
    @Override
    public List<CompilationDto> findCompilations(boolean pinned, int from, int size) {
        return List.of();
    }

    @Override
    public CompilationDto findCompilationsByCompId(long compId) {
        return null;
    }
}
