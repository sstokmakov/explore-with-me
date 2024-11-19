package ru.tokmakov.service.user;

import ru.tokmakov.dto.event.*;
import ru.tokmakov.dto.participation.ParticipationRequestDto;

import java.util.List;

public interface UserEventService {
    List<EventShortDto> findEventsAddedByUser(Long userId, Integer from, Integer size);

    EventFullDto saveEvent(Long userId, NewEventDto newEventDto);

    EventFullDto findEvent(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> findParticipation(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateParticipation(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);
}
