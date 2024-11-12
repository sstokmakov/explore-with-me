package user.service;

import dto.event.ParticipationRequestDto;

public interface UserRequestService {
    ParticipationRequestDto findUserParticipationRequests(Long userId);

    ParticipationRequestDto saveParticipationRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId);
}
