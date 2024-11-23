package ru.tokmakov.service.guest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class GuestEventsServiceImpl implements GuestEventsService {
    private final EventRepository eventRepository;
    private final StatClient statClient;


    @Override
    @Transactional
    public List<EventShortDto> findEvents(String text,
                                          List<Integer> categories,
                                          Boolean paid,
                                          String rangeStart,
                                          String rangeEnd,
                                          Boolean onlyAvailable,
                                          SortType sort,
                                          Integer from,
                                          Integer size,
                                          HttpServletRequest request) {
        log.info("Finding events with filters - text: {}, categories: {}, paid: {}, rangeStart: {}, rangeEnd: {}, onlyAvailable: {}, sort: {}, from: {}, size: {}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        LocalDateTime start = parseDateTime(rangeStart).orElse(null);
        LocalDateTime end = parseDateTime(rangeEnd).orElse(null);

        text = text != null ? "%" + text + "%" : "%";

        String sortField = sort == SortType.VIEWS ? "views" : "eventDate";
        Sort sortBy = Sort.by(sortField);

        Pageable pageable = PageRequest.of(from / size, size, sortBy);

        Page<Event> events;
        if (start == null || end == null) {
            events = eventRepository.findEventsWithFiltersWithoutDate(text, categories, paid, onlyAvailable, LocalDateTime.now(), pageable);
        } else {
            events = eventRepository.findEventsWithFilters(text, categories, paid, start, end, onlyAvailable, pageable);
        }

        statClient.recordHit(new HitDto("ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

        List<EventShortDto> list = events.stream()
                .map(EventMapper::toEventShortDto)
                .toList();

        if (list.isEmpty()) {
            log.warn("No events found matching the criteria");
            throw new BadRequestException("Event must be published");
        }

        log.info("Found {} events matching the filters", list.size());
        return list;
    }

    @Override
    @Transactional
    public EventFullDto findEventById(long id, HttpServletRequest request) {
        log.info("Finding event by id={} and checking for publication state", id);

        Event event = eventRepository.findById(id).orElse(null);

        if (event == null || event.getState() != EventState.PUBLISHED) {
            log.error("Event with id={} not found or not published", id);
            throw new NotFoundException("Event with id=" + id + " not found");
        }

        if (!statClient.existsByIpAndUri(request.getRemoteAddr(), request.getRequestURI())) {
            event.setViews(event.getViews() + 1);
            event = eventRepository.save(event);
            log.info("Incremented view count for event with id={}", id);
        }

        statClient.recordHit(new HitDto("ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

        log.info("Returning full event details for event with id={}", id);
        return EventMapper.toEventFullDto(event);
    }

    private Optional<LocalDateTime> parseDateTime(String dateTime) {
        try {
            return dateTime != null ? Optional.of(LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))) : Optional.empty();
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Invalid date format: " + dateTime);
        }
    }
}