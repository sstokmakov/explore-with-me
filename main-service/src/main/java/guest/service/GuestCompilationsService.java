package guest.service;

import dto.complation.CompilationDto;

import java.util.List;

public interface GuestCompilationsService {
    List<CompilationDto> findCompilations(boolean pinned, int from, int size);

    CompilationDto findCompilationsByCompId(long compId);
}