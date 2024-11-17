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
import ru.tokmakov.exception.BadRequestException;
import ru.tokmakov.exception.NotFoundException;
import ru.tokmakov.model.Compilation;
import ru.tokmakov.repository.CompilationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuestCompilationsServiceImpl implements GuestCompilationsService {
    private final CompilationRepository compilationRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> findCompilations(Boolean pinned, int from, int size) {
        if (from < 0 || size <= 0) {
            throw new BadRequestException("Invalid pagination parameters: 'from' must be >= 0 and 'size' must be > 0");
        }

        Pageable pageable = PageRequest.of(from / size, size);
        Page<Compilation> compilations;

        if (pinned != null) {
            compilations = compilationRepository.findByPinned(pinned, pageable);
        } else {
            compilations = compilationRepository.findAll(pageable);
        }

        return compilations.getContent().stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto findCompilationsByCompId(long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));

        return CompilationMapper.toCompilationDto(compilation);
    }
}