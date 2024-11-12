package ru.tokmakov.user.service;

import ru.tokmakov.dto.event.ParticipationRequestDto;

public interface UserRequestService {
    ParticipationRequestDto findUserParticipationRequests(Long userId);

    ParticipationRequestDto saveParticipationRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId);
}
