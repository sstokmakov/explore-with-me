package ru.tokmakov.controller.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.tokmakov.dto.participation.ParticipationRequestDto;
import ru.tokmakov.service.user.UserRequestService;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/users/{userId}/requests")
public class UserRequestController {
    private final UserRequestService userRequestService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> findUserParticipationRequests(@PathVariable long userId) {
        log.info("GET /users/{}/requests - Fetching participation requests for userId={}", userId, userId);

        List<ParticipationRequestDto> requests = userRequestService.findUserParticipationRequests(userId);

        log.info("GET /users/{}/requests - Found {} participation requests", userId, requests.size());
        return requests;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto saveParticipationRequest(@PathVariable long userId,
                                                            @RequestParam long eventId) {
        log.info("POST /users/{}/requests - Creating participation request for userId={}, eventId={}", userId, userId, eventId);

        ParticipationRequestDto request = userRequestService.saveParticipationRequest(userId, eventId);

        log.info("POST /users/{}/requests - Participation request created: {}", userId, request);
        return request;
    }

    @PatchMapping("/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable long userId,
                                                              @PathVariable long requestId) {
        log.info("PATCH /users/{}/requests/{}/cancel - Cancelling participation request for userId={}, requestId={}", userId, requestId, userId, requestId);

        ParticipationRequestDto cancelledRequest = userRequestService.cancelParticipationRequest(userId, requestId);

        log.info("PATCH /users/{}/requests/{}/cancel - Participation request cancelled: {}", userId, requestId, cancelledRequest);
        return cancelledRequest;
    }
}
