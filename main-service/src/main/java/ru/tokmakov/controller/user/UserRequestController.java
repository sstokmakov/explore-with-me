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
        return userRequestService.findUserParticipationRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto saveParticipationRequest(@PathVariable long userId,
                                                            @RequestParam long eventId) {
        return userRequestService.saveParticipationRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable long userId,
                                                              @PathVariable long requestId) {
        return userRequestService.cancelParticipationRequest(userId, requestId);
    }
}
