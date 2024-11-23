package ru.tokmakov.service.guest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.tokmakov.dto.complation.CompilationDto;
import org.springframework.stereotype.Service;
import ru.tokmakov.dto.complation.CompilationMapper;
import ru.tokmakov.exception.NotFoundException;
import ru.tokmakov.model.Compilation;
import ru.tokmakov.repository.CompilationRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuestCompilationsServiceImpl implements GuestCompilationsService {
    private final CompilationRepository compilationRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> findCompilations(Boolean pinned, Integer from, Integer size) {
        log.info("Fetching compilations with pinned: {}, from: {}, size: {}", pinned, from, size);
        Pageable pageable = PageRequest.of(from / size, size);
        Page<Compilation> compilations = pinned == null ?
                compilationRepository.findAll(pageable)
                :
                compilationRepository.findByPinned(pinned, pageable);

        List<CompilationDto> compilationDtos = compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .toList();
        log.info("Fetched {} compilations", compilationDtos.size());
        return compilationDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto findCompilationsByCompId(Long compId) {
        log.info("Fetching compilation by compId: {}", compId);
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> {
                    log.error("Compilation with id={} not found", compId);
                    return new NotFoundException("Compilation with id=" + compId + " was not found");
                });

        log.info("Compilation with id={} found: {}", compId, compilation);

        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilation);
        log.info("Converted compilation to DTO: {}", compilationDto);
        return compilationDto;
    }
}