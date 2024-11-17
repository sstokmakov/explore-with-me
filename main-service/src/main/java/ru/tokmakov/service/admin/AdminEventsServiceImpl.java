package ru.tokmakov.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.tokmakov.exception.NotFoundException;
import ru.tokmakov.dto.event.*;
import org.springframework.stereotype.Service;
import ru.tokmakov.exception.event.EventDateException;
import ru.tokmakov.exception.event.EventStateException;
import ru.tokmakov.model.Event;
import ru.tokmakov.repository.CategoriesRepository;
import ru.tokmakov.repository.EventRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminEventsServiceImpl implements AdminEventsService {
    private final EventRepository eventRepository;
    private final CategoriesRepository adminCategoriesRepository;


    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> findEvents(Set<Long> users, Set<EventState> states, Set<Long> categories,
                                         String rangeStart, String rangeEnd, int from, int size) {

        LocalDateTime start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findByFilters(users, states, categories, start, end, pageable);

        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(long eventId, UpdateEventAdminRequest eventShortDto) {
        log.info("Updating event with ID: {}", eventId);

        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Event with ID {} not found", eventId);
            return new NotFoundException("Event with id=" + eventId + " not found");
        });

        log.info("Event found: {}", event);

        LocalDateTime eventDate = LocalDateTime.parse(eventShortDto.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
            log.error("Attempt to update event with ID {} to an invalid date: {}", eventId, eventShortDto.getEventDate());
            throw new EventDateException("Event date must be at least one hour from the current time.");
        }
        log.info("Event date updated successfully to: {}", eventDate);

        if (AdminStateAction.PUBLISH_EVENT.equals(eventShortDto.getStateAction())) {
            if (!event.getState().equals(EventState.PENDING)) {
                log.error("Cannot publish the event with ID {} because it's not in the right state: {}", eventId, event.getState());
                throw new EventStateException("Cannot publish the event because it's not in the right state: " + event.getState());
            }
            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
            log.info("Event with ID {} published successfully", eventId);
        } else if (AdminStateAction.REJECT_EVENT.equals(eventShortDto.getStateAction())) {
            if (event.getState().equals(EventState.PUBLISHED)) {
                log.error("Cannot cancel the event with ID {} because it's already published", eventId);
                throw new EventStateException("Cannot cancel the event because it's already published.");
            }
            event.setState(EventState.CANCELED);
            log.info("Event with ID {} rejected successfully", eventId);
        }

        event.setAnnotation(eventShortDto.getAnnotation());
        event.setCategory(adminCategoriesRepository.findById(eventShortDto.getCategory()).orElseThrow(() -> {
            log.error("Category with ID {} not found", eventShortDto.getCategory());
            return new NotFoundException("category with id " + eventShortDto.getCategory() + " not found");
        }));
        event.setDescription(eventShortDto.getDescription());
        event.setEventDate(eventDate);
        event.setLat(eventShortDto.getLocation().getLat());
        event.setLon(eventShortDto.getLocation().getLon());
        event.setPaid(eventShortDto.getPaid());
        event.setParticipantLimit(eventShortDto.getParticipantLimit());
        event.setRequestModeration(eventShortDto.getRequestModeration());
        event.setTitle(eventShortDto.getTitle());

        Event updatedEvent = eventRepository.save(event);
        log.info("Event with ID {} updated successfully", eventId);

        return EventMapper.toEventFullDto(updatedEvent);
    }
}