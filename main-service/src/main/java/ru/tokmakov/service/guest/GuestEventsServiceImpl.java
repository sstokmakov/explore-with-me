package ru.tokmakov.service.guest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.tokmakov.dto.HitDto;
import ru.tokmakov.dto.event.*;
import org.springframework.stereotype.Service;
import ru.tokmakov.exception.BadRequestException;
import ru.tokmakov.exception.NotFoundException;
import ru.tokmakov.model.Event;
import ru.tokmakov.repository.EventRepository;
import ru.tokmakov.stat.StatClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GuestEventsServiceImpl implements GuestEventsService {
    private final EventRepository eventRepository;
    private final StatClient statClient;


    @Override
    public List<EventShortDto> findEvents(String text,
                                          List<Integer> categories,
                                          Boolean paid,
                                          String rangeStart,
                                          String rangeEnd,
                                          Boolean onlyAvailable,
                                          SortType sort,
                                          int from,
                                          int size,
                                          HttpServletRequest request) {
        LocalDateTime start = parseDateTime(rangeStart).orElse(null);
        LocalDateTime end = parseDateTime(rangeEnd).orElse(null);

        if (start == null || end == null) {
            start = LocalDateTime.now();
            end = LocalDateTime.now();
        }

        text = text.toLowerCase();

        Pageable pageable = PageRequest.of(from / size, size);
        Page<Event> events = sort == SortType.EVENT_DATE ?
                eventRepository.findEventsWithFiltersOrderByDate(text, categories, paid, start, end, onlyAvailable, pageable)
                :
                eventRepository.findEventsWithFiltersOrderByViews(text, categories, paid, start, end, onlyAvailable, pageable);

        statClient.recordHit(new HitDto("ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

        return events.stream()
                .map(EventMapper::toEventShortDto)
                .toList();
    }

    @Override
    public EventFullDto findEventById(long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id).orElse(null);

        if (event == null || event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException("Event with id=" + id + " not found");
        }

        statClient.recordHit(new HitDto("ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

        return EventMapper.toEventFullDto(event);
    }

    private Optional<LocalDateTime> parseDateTime(String dateTime) {
        try {
            return dateTime != null ? Optional.of(LocalDateTime.parse(dateTime)) : Optional.empty();
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Invalid date format: " + dateTime);
        }
    }
}