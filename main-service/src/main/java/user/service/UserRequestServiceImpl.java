package user.service;

import org.springframework.stereotype.Service;
import dto.event.ParticipationRequestDto;

@Service
public class UserRequestServiceImpl implements UserRequestService {
    @Override
    public ParticipationRequestDto findUserParticipationRequests(Long userId) {
        return null;
    }

    @Override
    public ParticipationRequestDto saveParticipationRequest(Long userId, Long eventId) {
        return null;
    }

    @Override
    public ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId) {
        return null;
    }
}
