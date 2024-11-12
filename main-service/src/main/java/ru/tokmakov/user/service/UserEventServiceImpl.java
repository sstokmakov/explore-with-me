package ru.tokmakov.user.service;

import ru.tokmakov.dto.event.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserEventServiceImpl implements UserEventService {
    @Override
    public List<EventShortDto> findEventsAddedByUser(Long userId, int from, int size) {
        return List.of();
    }

    @Override
    public EventFullDto saveEvent(Long userId, NewEventDto newEventDto) {
        return null;
    }

    @Override
    public EventFullDto findEvent(Long userId, Long eventId) {
        return null;
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        return null;
    }

    @Override
    public ParticipationRequestDto findParticipation(Long userId, Long eventId) {
        return null;
    }

    @Override
    public EventRequestStatusUpdateResult updateParticipation(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return null;
    }
}