package ru.tokmakov.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.tokmakov.exception.TitleAlreadyExistsException;
import ru.tokmakov.repository.CompilationRepository;
import ru.tokmakov.dto.complation.CompilationDto;
import ru.tokmakov.dto.complation.CompilationMapper;
import ru.tokmakov.dto.complation.NewCompilationDto;
import ru.tokmakov.dto.complation.UpdateCompilationRequest;
import org.springframework.stereotype.Service;
import ru.tokmakov.exception.NotFoundException;
import ru.tokmakov.model.Compilation;
import ru.tokmakov.model.Event;
import ru.tokmakov.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCompilationsServiceImpl implements AdminCompilationsService {
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    @Override
    @Transactional
    public CompilationDto saveCompilations(NewCompilationDto newCompilationDto) {
        log.info("Saving new compilation with title: {}", newCompilationDto.getTitle());

        if (compilationRepository.existsByTitle(newCompilationDto.getTitle())) {
            String message = "Title " + newCompilationDto.getTitle() + " already exists";
            log.error(message);
            throw new TitleAlreadyExistsException(message);
        }

        Compilation compilation = new Compilation();
        compilation.setTitle(newCompilationDto.getTitle());
        compilation.setPinned(newCompilationDto.getPinned() != null ? newCompilationDto.getPinned() : false);

        List<Event> events = eventRepository.findAllByIds(newCompilationDto.getEvents());
        compilation.setEvents(events != null ? events : new ArrayList<>());

        Compilation savedCompilation = compilationRepository.save(compilation);

        log.info("Compilation saved successfully with ID: {}", savedCompilation.getId());
        return CompilationMapper.toCompilationDto(savedCompilation);
    }

    @Override
    @Transactional
    public void deleteCompilations(long compId) {
        log.info("Deleting compilation with ID: {}", compId);

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> {
                    String message = "Compilation with id=" + compId + " was not found";
                    log.error(message);
                    return new NotFoundException(message);
                });

        compilationRepository.delete(compilation);

        log.info("Compilation with ID: {} deleted successfully", compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilations(long compId, UpdateCompilationRequest newCompilationDto) {
        log.info("Updating compilation with ID: {}", compId);

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> {
                    String message = "Compilation with id=" + compId + " was not found";
                    log.error(message);
                    return new NotFoundException(message);
                });

        if (newCompilationDto.getTitle() != null && !newCompilationDto.getTitle().isBlank()) {
            compilation.setTitle(newCompilationDto.getTitle());
            log.info("Updated title to: {}", newCompilationDto.getTitle());
        }

        if (newCompilationDto.getPinned() != null) {
            compilation.setPinned(newCompilationDto.getPinned());
            log.info("Updated pinned status to: {}", newCompilationDto.getPinned());
        }

        if (newCompilationDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
            if (events.size() != newCompilationDto.getEvents().size()) {
                String message = "Some events in the list were not found";
                log.error(message);
                throw new NotFoundException(message);
            }
            compilation.setEvents(events);
            log.info("Updated events: {}", newCompilationDto.getEvents());
        }

        compilation = compilationRepository.save(compilation);

        log.info("Compilation with ID: {} updated successfully", compId);
        return CompilationMapper.toCompilationDto(compilation);
    }
}