package ru.tokmakov.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tokmakov.exception.NotFoundException;
import ru.tokmakov.repository.UserRepository;
import ru.tokmakov.dto.event.EventState;
import ru.tokmakov.dto.participation.ParticipationRequestDto;
import ru.tokmakov.dto.event.RequestStatus;
import ru.tokmakov.dto.participation.ParticipationRequestMapper;
import ru.tokmakov.exception.ConflictException;
import ru.tokmakov.model.Event;
import ru.tokmakov.model.ParticipationRequest;
import ru.tokmakov.model.User;
import ru.tokmakov.repository.EventRepository;
import ru.tokmakov.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRequestServiceImpl implements UserRequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> findUserParticipationRequests(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));

        List<ParticipationRequest> requests = requestRepository.findByRequesterId(userId);

        return requests.stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto saveParticipationRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Event initiator can not request participation in their own event.");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Cannot participate in an unpublished event.");
        }

        boolean requestExists = requestRepository.existsByRequesterIdAndEventId(userId, eventId);
        if (requestExists) {
            throw new ConflictException("Request already exists for this user and event.");
        }

        if (event.getParticipantLimit() != 0
            && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("Event participation limit has been reached.");
        }

        ParticipationRequest request = new ParticipationRequest();
        request.setEvent(event);
        request.setRequester(user);
        request.setCreated(LocalDateTime.now());

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }

        ParticipationRequest savedRequest = requestRepository.save(request);

        return ParticipationRequestMapper.toParticipationRequestDto(savedRequest);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId) {
        log.info("Received request to cancel participation for userId={} and requestId={}", userId, requestId);

        ParticipationRequest request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> {
                    log.error("Request with id={} not found for userId={}", requestId, userId);
                    return new NotFoundException("Request with id=" + requestId + " was not found");
                });

        request.setStatus(RequestStatus.CANCELED);
        requestRepository.save(request);

        log.info("Successfully cancelled participation for userId={} and requestId={}", userId, requestId);

        return ParticipationRequestMapper.toParticipationRequestDto(request);
    }
}