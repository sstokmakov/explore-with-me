package ru.tokmakov.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.tokmakov.exception.BadRequestException;
import ru.tokmakov.exception.EventStateException;
import ru.tokmakov.exception.NotFoundException;
import ru.tokmakov.dto.event.*;
import org.springframework.stereotype.Service;
import ru.tokmakov.model.Event;
import ru.tokmakov.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;


    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> findEvents(Set<Long> users, Set<EventState> states, Set<Long> categories,
                                         String rangeStart, String rangeEnd, Integer from, Integer size) {

        log.info("Finding events with parameters - users: {}, states: {}, categories: {}, rangeStart: {}, rangeEnd: {}, from: {}, size: {}",
                users, states, categories, rangeStart, rangeEnd, from, size);

        LocalDateTime start;
        LocalDateTime end;

        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> events;

        if (rangeStart == null || rangeEnd == null) {
            log.info("No date range provided, fetching events without date filter.");
            events = eventRepository.findByFiltersWithoutDate(users, states, categories, LocalDateTime.now(), pageable);
        } else {
            start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            log.info("Fetching events within date range: {} to {}", start, end);
            events = eventRepository.findByFilters(users, states, categories, start, end, pageable);
        }

        log.info("Found {} events matching the criteria", events.size());

        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Updating event with ID: {}", eventId);

        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Event with ID {} not found", eventId);
            return new NotFoundException("Event with id=" + eventId + " not found");
        });

        log.info("Event found: {}", event);

        updateFields(event, updateEventAdminRequest);

        Event updatedEvent = eventRepository.save(event);
        log.info("Event with ID {} updated successfully", eventId);

        return EventMapper.toEventFullDto(updatedEvent);
    }

    private void updateFields(Event event, UpdateEventAdminRequest updateEventAdminRequest) {
        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEventAdminRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category with id " + updateEventAdminRequest.getCategory() + " not found")));
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(updateEventAdminRequest.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if (eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
                throw new BadRequestException("Event date must be at least one hour from the current time.");
            }
            event.setEventDate(eventDate);
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLat(updateEventAdminRequest.getLocation().getLat());
            event.setLon(updateEventAdminRequest.getLocation().getLon());
        }
        if (updateEventAdminRequest.getPaid() != null)
            event.setPaid(updateEventAdminRequest.getPaid());
        if (updateEventAdminRequest.getParticipantLimit() != null)
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        if (updateEventAdminRequest.getRequestModeration() != null)
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        if (updateEventAdminRequest.getStateAction() != null) {
            if (AdminStateAction.PUBLISH_EVENT.equals(updateEventAdminRequest.getStateAction())) {
                if (!event.getState().equals(EventState.PENDING)) {
                    log.error("Cannot publish the event with ID {} because it's not in the right state: {}", event.getId(), event.getState());
                    throw new EventStateException("Cannot publish the event because it's not in the right state: " + event.getState());
                }
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
                log.info("Event with ID {} published successfully", event.getId());
            } else if (AdminStateAction.REJECT_EVENT.equals(updateEventAdminRequest.getStateAction())) {
                if (event.getState().equals(EventState.PUBLISHED)) {
                    log.error("Cannot cancel the event with ID {} because it's already published", event.getId());
                    throw new EventStateException("Cannot cancel the event because it's already published.");
                }
                event.setState(EventState.CANCELED);
                log.info("Event with ID {} rejected successfully", event.getId());
            }
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
    }
}