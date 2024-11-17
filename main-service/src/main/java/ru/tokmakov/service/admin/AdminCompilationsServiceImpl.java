package ru.tokmakov.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tokmakov.repository.CompilationRepository;
import ru.tokmakov.dto.complation.CompilationDto;
import ru.tokmakov.dto.complation.CompilationMapper;
import ru.tokmakov.dto.complation.NewCompilationDto;
import ru.tokmakov.dto.complation.UpdateCompilationRequest;
import org.springframework.stereotype.Service;
import ru.tokmakov.exception.NotFoundException;
import ru.tokmakov.exception.compilation.TitleAlreadyExistsException;
import ru.tokmakov.model.Compilation;
import ru.tokmakov.model.Event;
import ru.tokmakov.repository.EventRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCompilationsServiceImpl implements AdminCompilationsService {
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    @Override
    public CompilationDto saveCompilations(NewCompilationDto newCompilationDto) {
        Compilation compilation = new Compilation();
        compilation.setTitle(newCompilationDto.getTitle());
        compilation.setPinned(newCompilationDto.getPinned());

        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            Set<Event> events = eventRepository.findAllByIds(newCompilationDto.getEvents());
            compilation.setEvents(events);
        }

        if (compilationRepository.existsByTitle(compilation.getTitle())) {
            throw new TitleAlreadyExistsException("title " + compilation.getTitle() + " already exists");
        }

        Compilation savedCompilation = compilationRepository.save(compilation);

        return CompilationMapper.toCompilationDto(savedCompilation);
    }

    @Override
    public void deleteCompilations(long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));

        compilationRepository.delete(compilation);
    }

    @Override
    public CompilationDto updateCompilations(long compId, UpdateCompilationRequest newCompilationDto) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));

        if (newCompilationDto.getTitle() != null && !newCompilationDto.getTitle().isBlank()) {
            compilation.setTitle(newCompilationDto.getTitle());
        }
        if (newCompilationDto.getPinned() != null) {
            compilation.setPinned(newCompilationDto.getPinned());
        }
        if (newCompilationDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
            if (events.size() != newCompilationDto.getEvents().size()) {
                throw new NotFoundException("Some events in the list were not found");
            }
            compilation.setEvents(new HashSet<>(events));
        }

        compilation = compilationRepository.save(compilation);

        return CompilationMapper.toCompilationDto(compilation);
    }
}