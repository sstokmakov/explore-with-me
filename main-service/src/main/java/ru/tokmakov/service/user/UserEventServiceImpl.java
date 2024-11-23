package ru.tokmakov.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import ru.tokmakov.dto.participation.ParticipationRequestDto;
import ru.tokmakov.dto.participation.ParticipationRequestMapper;
import ru.tokmakov.exception.BadRequestException;
import ru.tokmakov.exception.ConflictException;
import ru.tokmakov.exception.NotFoundException;
import ru.tokmakov.exception.OperationPreconditionFailedException;
import ru.tokmakov.repository.CategoryRepository;
import ru.tokmakov.repository.UserRepository;
import ru.tokmakov.dto.event.*;
import org.springframework.stereotype.Service;
import ru.tokmakov.model.Category;
import ru.tokmakov.model.Event;
import ru.tokmakov.model.ParticipationRequest;
import ru.tokmakov.model.User;
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
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> findEventsAddedByUser(Long userId, Integer from, Integer size) {
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
    @Transactional
    public EventFullDto saveEvent(Long userId, NewEventDto newEventDto) {
        log.info("Attempting to save event for userId: {}, with data: {}", userId, newEventDto);

        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id {} not found", userId);
            return new NotFoundException("User with id " + userId + " not found");
        });

        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() -> {
            log.error("Category with id {} not found", newEventDto.getCategory());
            return new NotFoundException("Category with id " + newEventDto.getCategory() + " not found");
        });

        LocalDateTime eventDate = LocalDateTime.parse(newEventDto.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (eventDate.plusHours(2).isBefore(LocalDateTime.now())) {
            log.error("Event date {} is invalid, the event cannot occur earlier than two hours from the current moment", newEventDto.getEventDate());
            throw new BadRequestException("Event cannot occur earlier than two hours from the current moment");
        }

        Event event = EventMapper.newEventDtoToEvent(newEventDto);
        event.setCategory(category);
        event.setEventDate(eventDate);
        event.setInitiator(user);

        Event savedEvent = eventRepository.save(event);

        log.info("Event successfully saved with id: {}", savedEvent.getId());

        return EventMapper.toEventFullDto(savedEvent);
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        log.info("Start updating event. User ID: {}, Event ID: {}", userId, eventId);

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> {
                    log.error("Event with id={} not found", eventId);
                    return new NotFoundException("Event with id=" + eventId + " was not found");
                });

        log.info("Event state: {}", event.getState());
        if (event.getState() != EventState.PENDING && event.getState() != EventState.CANCELED) {
            log.error("Event with id={} cannot be changed because it is neither PENDING or CANCELED", eventId);
            throw new OperationPreconditionFailedException("Only pending or canceled events can be changed");
        }

        log.info("Updating event details for event ID: {}", eventId);
        updateEventField(event, updateEventUserRequest);
        eventRepository.save(event);
        log.info("Event with id={} updated successfully", eventId);

        return EventMapper.toEventFullDto(event);
    }

    private void updateEventField(Event event, UpdateEventUserRequest updateEventUserRequest) {
        if (updateEventUserRequest.getAnnotation() != null)
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        if (updateEventUserRequest.getCategory() != null)
            event.setCategory(categoryRepository.findById(updateEventUserRequest.getCategory()).orElseThrow(
                    () -> new NotFoundException("Category with id=" + updateEventUserRequest.getCategory() + " was not found")
            ));
        if (updateEventUserRequest.getDescription() != null)
            event.setDescription(updateEventUserRequest.getDescription());
        if (updateEventUserRequest.getEventDate() != null) {
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime eventDate = LocalDateTime.parse(updateEventUserRequest.getEventDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if (eventDate.isBefore(currentTime.plusHours(2))) {
                log.error("Event date for event with id={} is too soon. Provided: {}, Current time: {}", event.getId(), eventDate, currentTime);
                throw new BadRequestException("Event date must be at least 2 hours from now.");
            }
        }
        if (updateEventUserRequest.getLocation() != null) {
            event.setLat(updateEventUserRequest.getLocation().getLat());
            event.setLon(updateEventUserRequest.getLocation().getLon());
        }
        if (updateEventUserRequest.getPaid() != null)
            event.setPaid(updateEventUserRequest.getPaid());
        if (updateEventUserRequest.getParticipantLimit() != null)
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        if (updateEventUserRequest.getRequestModeration() != null)
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        if (updateEventUserRequest.getStateAction() != null) {
            if (updateEventUserRequest.getStateAction() == UserStateAction.CANCEL_REVIEW)
                event.setState(EventState.CANCELED);
            else
                event.setState(EventState.PENDING);
        }
        if (updateEventUserRequest.getTitle() != null)
            event.setTitle(updateEventUserRequest.getTitle());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> findParticipation(Long userId, Long eventId) {
        log.info("Fetching participation requests for userId: {}, eventId: {}", userId, eventId);

        userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id={} not found", userId);
            return new NotFoundException("User with id=" + userId + " not found");
        });

        eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Event with id={} not found", eventId);
            return new NotFoundException("Event with id=" + eventId + " not found");
        });

        List<ParticipationRequest> requests = requestRepository.findAllByEventId(eventId);

        List<ParticipationRequestDto> requestDtos = requests.stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
        log.info("Returning {} participation requests for userId={} and eventId={}", requestDtos.size(), userId, eventId);

        return requestDtos;
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateParticipation(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("Received request to update participation for userId={} and eventId={}", userId, eventId);

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> {
                    log.error("Event with id={} not found or not accessible for userId={}", eventId, userId);
                    return new NotFoundException("Event with id=" + eventId + " not found or not accessible");
                });

        log.info("Found event: {} with participant limit {}", eventId, event.getParticipantLimit());

        List<ParticipationRequest> requests = requestRepository.findAllById(eventRequestStatusUpdateRequest.getRequestIds());
        log.info("Processing {} requests for eventId={}", requests.size(), eventId);

        for (ParticipationRequest request : requests) {
            if (request.getStatus() != RequestStatus.PENDING && !(request.getStatus() == RequestStatus.CONFIRMED && request.getEvent().getParticipantLimit() == 0)) {
                log.warn("Request with id={} has status {} instead of PENDING", request.getId(), request.getStatus());
                throw new ConflictException("Request must have status PENDING");
            }
        }

        List<ParticipationRequest> confirmedRequests = new ArrayList<>();
        List<ParticipationRequest> rejectedRequests = new ArrayList<>();
        int confirmedCount = event.getConfirmedRequests();

        for (ParticipationRequest request : requests) {
            log.info("Processing request with id={} for status {}", request.getId(), eventRequestStatusUpdateRequest.getStatus());

            if (eventRequestStatusUpdateRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
                if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
                    request.setStatus(RequestStatus.CONFIRMED);
                    confirmedRequests.add(request);
                    confirmedCount++;
                    log.info("Request with id={} automatically confirmed for eventId={}", request.getId(), eventId);
                } else if (confirmedCount <= event.getParticipantLimit()) {
                    request.setStatus(RequestStatus.CONFIRMED);
                    confirmedRequests.add(request);
                    confirmedCount++;
                    log.info("Request with id={} confirmed for eventId={} (confirmedCount={})", request.getId(), eventId, confirmedCount);
                } else {
                    rejectedRequests.add(request);
                    log.info("Confirmed count >= participant limit request with id={} rejected for eventId={}", request.getId(), eventId);
                }
            } else if (eventRequestStatusUpdateRequest.getStatus().equals(RequestStatus.REJECTED)) {
                request.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(request);
                log.info("Request with id={} rejected for eventId={}", request.getId(), eventId);
            }
        }

        requestRepository.saveAll(confirmedRequests);
        requestRepository.saveAll(rejectedRequests);

        log.info("Saved {} confirmed and {} rejected requests for eventId={}", confirmedRequests.size(), rejectedRequests.size(), eventId);

        event.setConfirmedRequests(confirmedCount);
        eventRepository.save(event);
        log.info("Updated confirmed requests count to {} for eventId={}", confirmedCount, eventId);

        if (event.getParticipantLimit() != 0 && confirmedCount > event.getParticipantLimit()) {
            throw new ConflictException("The participant limit has been reached, all pending requests have been rejected");
        }

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult(
                confirmedRequests.stream().map(ParticipationRequestMapper::toParticipationRequestDto).toList(),
                rejectedRequests.stream().map(ParticipationRequestMapper::toParticipationRequestDto).toList()
        );

        log.info("Returning result for eventId={} with {} confirmed and {} rejected requests", eventId, confirmedRequests.size(), rejectedRequests.size());

        return result;
    }
}