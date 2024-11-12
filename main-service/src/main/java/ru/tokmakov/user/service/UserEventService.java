package ru.tokmakov.user.service;

import ru.tokmakov.dto.event.*;

import java.util.List;

public interface UserEventService {
    List<EventShortDto> findEventsAddedByUser(Long userId, int from, int size);

    EventFullDto saveEvent(Long userId, NewEventDto newEventDto);

    EventFullDto findEvent(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    ParticipationRequestDto findParticipation(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateParticipation(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);
}
