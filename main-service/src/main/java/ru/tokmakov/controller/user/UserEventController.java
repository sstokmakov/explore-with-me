package ru.tokmakov.controller.user;

import org.springframework.validation.annotation.Validated;
import ru.tokmakov.dto.event.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.tokmakov.dto.participation.ParticipationRequestDto;
import ru.tokmakov.service.user.UserEventService;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/users/{userId}/events")
public class UserEventController {
    private final UserEventService userEventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> findEventsAddedByUser(@PathVariable Long userId,
                                                     @RequestParam(required = false, defaultValue = "0") Integer from,
                                                     @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("GET /users/{}/events - Fetching events added by userId={}, from={}, size={}", userId, userId, from, size);

        List<EventShortDto> eventShortDtos = userEventService.findEventsAddedByUser(userId, from, size);

        log.info("GET /users/{}/events - Found {} events", userId, eventShortDtos.size());
        return eventShortDtos;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto saveEvent(@PathVariable Long userId, @Validated @NotNull @RequestBody NewEventDto newEventDto) {
        log.info("POST /users/{}/events - Saving new event for userId={}, eventDto={}", userId, userId, newEventDto);

        EventFullDto savedEvent = userEventService.saveEvent(userId, newEventDto);

        log.info("POST /users/{}/events - Event saved: {}", userId, savedEvent);
        return savedEvent;
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto findEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("GET /users/{}/events/{} - Fetching event details for userId={}, eventId={}", userId, eventId, userId, eventId);

        EventFullDto eventFullDto = userEventService.findEvent(userId, eventId);

        log.info("GET /users/{}/events/{} - Event details fetched: {}", userId, eventId, eventFullDto);
        return eventFullDto;
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable long userId,
                                    @PathVariable long eventId,
                                    @Validated @NotNull @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("PATCH /users/{}/events/{} - Updating event for userId={}, eventId={}, updateRequest={}", userId, eventId, userId, eventId, updateEventUserRequest);

        EventFullDto event = userEventService.updateEvent(userId, eventId, updateEventUserRequest);

        log.info("PATCH /users/{}/events/{} - Event updated: {}", userId, eventId, event);
        return event;
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> findParticipation(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("GET /users/{}/events/{}/requests - Fetching participation requests for userId={}, eventId={}", userId, eventId, userId, eventId);

        List<ParticipationRequestDto> participationRequests = userEventService.findParticipation(userId, eventId);

        log.info("GET /users/{}/events/{}/requests - Found {} participation requests", userId, eventId, participationRequests.size());
        return participationRequests;
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateParticipation(@PathVariable Long userId,
                                                              @PathVariable Long eventId,
                                                              @Validated @NotNull @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("PATCH /users/{}/events/{}/requests - Updating participation requests for userId={}, eventId={}, updateRequest={}", userId, eventId, userId, eventId, eventRequestStatusUpdateRequest);

        EventRequestStatusUpdateResult result = userEventService.updateParticipation(userId, eventId, eventRequestStatusUpdateRequest);

        log.info("PATCH /users/{}/events/{}/requests - Participation requests updated: {}", userId, eventId, result);
        return result;
    }
}