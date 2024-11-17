package ru.tokmakov.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.tokmakov.dto.participation.ParticipationRequestDto;
import ru.tokmakov.dto.participation.ParticipationRequestMapper;
import ru.tokmakov.exception.BadRequestException;
import ru.tokmakov.exception.NotFoundException;
import ru.tokmakov.exception.event.ConflictException;
import ru.tokmakov.repository.CategoriesRepository;
import ru.tokmakov.repository.UserRepository;
import ru.tokmakov.dto.event.*;
import org.springframework.stereotype.Service;
import ru.tokmakov.exception.event.EventDateException;
import ru.tokmakov.exception.event.EventStateException;
import ru.tokmakov.exception.ForbiddenAccessException;
import ru.tokmakov.model.Category;
import ru.tokmakov.model.Event;
import ru.tokmakov.model.ParticipationRequest;
import ru.tokmakov.model.User;
import ru.tokmakov.exception.EventDateNotValidException;
import ru.tokmakov.repository.EventRepository;
import ru.tokmakov.repository.RequestRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEventServiceImpl implements UserEventService {
    private final UserRepository userRepository;
    private final CategoriesRepository categoriesRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    public List<EventShortDto> findEventsAddedByUser(Long userId, int from, int size) {
        log.info("Finding events added by user with ID: {}. Pagination - from: {}, size: {}", userId, from, size);

        userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with ID: {} not found", userId);
            return new NotFoundException("User with id " + userId + " not found");
        });

        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "eventDate"));
        log.debug("Pageable created: {}", pageable);

        Page<Event> eventsPage = eventRepository.findByInitiatorId(userId, pageable);
        log.info("Found {} events for user with ID: {}", eventsPage.getTotalElements(), userId);

        List<EventShortDto> eventShortDtos = eventsPage.getContent()
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());

        log.debug("Mapped {} events to EventShortDto for user with ID: {}", eventShortDtos.size(), userId);
        return eventShortDtos;
    }


    @Override
    public EventFullDto saveEvent(Long userId, NewEventDto newEventDto) {
        log.info("Attempting to save event for userId: {}, with data: {}", userId, newEventDto);

        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id {} not found", userId);
            return new NotFoundException("User with id " + userId + " not found");
        });

        log.debug("Found user: {}", user);

        Category category = categoriesRepository.findById(newEventDto.getCategory()).orElseThrow(() -> {
            log.error("Category with id {} not found", newEventDto.getCategory());
            return new NotFoundException("Category with id " + newEventDto.getCategory() + " not found");
        });

        log.debug("Found category: {}", category);

        LocalDateTime eventDate = LocalDateTime.parse(newEventDto.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (eventDate.isBefore(LocalDateTime.now())) {
            log.error("Event date {} is invalid, it must be in the future", newEventDto.getEventDate());
            throw new EventDateNotValidException("Event date must be in the future");
        }

        log.debug("Validated event date: {}", eventDate);

        Event event = EventMapper.newEventDtoToEvent(newEventDto);
        event.setCategory(category);
        event.setEventDate(eventDate);
        event.setInitiator(user);

        log.debug("Event object created: {}", event);

        Event savedEvent = eventRepository.save(event);

        log.info("Event successfully saved with id: {}", savedEvent.getId());

        return EventMapper.toEventFullDto(savedEvent);
    }

    @Override
    public EventFullDto findEvent(Long userId, Long eventId) {
        log.info("Received request to find event with ID: {} for user with ID: {}", eventId, userId);

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        log.info("Event with ID: {} found for user with ID: {}", eventId, userId);

        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);

        log.info("Returning full event details for event with ID: {}", eventId);
        return eventFullDto;
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        log.info("Start updating event. User ID: {}, Event ID: {}", userId, eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    log.error("Event with id={} not found", eventId);
                    return new NotFoundException("Event with id=" + eventId + " was not found");
                });

        if (!event.getInitiator().getId().equals(userId)) {
            log.error("User with id={} is not the initiator of the event with id={}", userId, eventId);
            throw new ForbiddenAccessException("User with id=" + userId + " is not the initiator of this event.");
        }

        log.info("Event state: {}", event.getState());
        if (!event.getState().equals(EventState.PENDING) && !event.getState().equals(EventState.CANCELED)) {
            log.error("Event with id={} cannot be changed because it is neither PENDING nor CANCELED", eventId);
            throw new EventStateException("Only pending or canceled events can be changed.");
        }

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime eventDate = LocalDateTime.parse(updateEventUserRequest.getEventDate(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (eventDate.isBefore(currentTime.plusHours(2))) {
            log.error("Event date for event with id={} is too soon. Provided: {}, Current time: {}", eventId, eventDate, currentTime);
            throw new EventDateException("Event date must be at least 2 hours from now.");
        }

        log.info("Updating event details for event ID: {}", eventId);
        event.setAnnotation(updateEventUserRequest.getAnnotation());
        event.setCategory(categoriesRepository.findById(updateEventUserRequest.getCategory())
                .orElseThrow(() -> {
                    log.error("Category with id={} not found", updateEventUserRequest.getCategory());
                    return new NotFoundException("Category with id=" + updateEventUserRequest.getCategory() + " not found");
                }));
        event.setDescription(updateEventUserRequest.getDescription());
        event.setEventDate(eventDate);
        event.setLat(updateEventUserRequest.getLocation().getLat());
        event.setLon(updateEventUserRequest.getLocation().getLon());
        event.setPaid(updateEventUserRequest.getPaid());
        event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        event.setTitle(updateEventUserRequest.getTitle());

        eventRepository.save(event);
        log.info("Event with id={} updated successfully", eventId);

        return EventMapper.toEventFullDto(event);
    }


    @Override
    public List<ParticipationRequestDto> findParticipation(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id=" + userId + " not found"));

        eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event with id=" + eventId + " not found"));

        List<ParticipationRequest> requests = requestRepository.findAllByEventIdAndRequesterId(eventId, userId);

        return requests.stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateParticipation(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " not found or not accessible"));

        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            throw new BadRequestException("No moderation required or participant limit is zero for this event");
        }

        List<ParticipationRequest> requests = requestRepository.findAllById(eventRequestStatusUpdateRequest.getRequestIds());
        if (requests.isEmpty()) {
            throw new NotFoundException("No requests found for the provided IDs");
        }

        for (ParticipationRequest request : requests) {
            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                throw new ConflictException("Request must have status PENDING");
            }
        }

        List<ParticipationRequest> confirmedRequests = new ArrayList<>();
        List<ParticipationRequest> rejectedRequests = new ArrayList<>();
        int confirmedCount = event.getConfirmedRequests();

        for (ParticipationRequest request : requests) {
            if (eventRequestStatusUpdateRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
                if (confirmedCount < event.getParticipantLimit()) {
                    request.setStatus(RequestStatus.CONFIRMED);
                    confirmedRequests.add(request);
                    confirmedCount++;
                } else {
                    request.setStatus(RequestStatus.REJECTED);
                    rejectedRequests.add(request);
                }
            } else if (eventRequestStatusUpdateRequest.getStatus().equals(RequestStatus.REJECTED)) {
                request.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(request);
            }
        }

        if (confirmedCount >= event.getParticipantLimit()) {
            List<ParticipationRequest> pendingRequests = requestRepository.findAllByEventIdAndStatus(eventId, RequestStatus.PENDING);
            for (ParticipationRequest request : pendingRequests) {
                request.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(request);
            }
        }

        requestRepository.saveAll(confirmedRequests);
        requestRepository.saveAll(rejectedRequests);

        event.setConfirmedRequests(confirmedCount);
        eventRepository.save(event);

        return new EventRequestStatusUpdateResult(
                confirmedRequests.stream().map(ParticipationRequestMapper::toParticipationRequestDto).toList(),
                rejectedRequests.stream().map(ParticipationRequestMapper::toParticipationRequestDto).toList()
        );
    }
}