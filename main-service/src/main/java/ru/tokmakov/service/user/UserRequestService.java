package ru.tokmakov.service.user;

import ru.tokmakov.dto.participation.ParticipationRequestDto;

import java.util.List;

public interface UserRequestService {
    List<ParticipationRequestDto> findUserParticipationRequests(Long userId);

    ParticipationRequestDto saveParticipationRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId);
}
