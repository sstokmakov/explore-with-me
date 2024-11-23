package ru.tokmakov.controller.admin;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.tokmakov.dto.event.EventState;
import ru.tokmakov.dto.event.UpdateEventAdminRequest;
import ru.tokmakov.service.admin.AdminEventsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.tokmakov.dto.event.EventFullDto;

import java.util.List;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/admin/events")
public class AdminEventsController {
    private final AdminEventsService adminEventsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> findEvents(@RequestParam(required = false) Set<Long> users,
                                         @RequestParam(required = false) Set<EventState> states,
                                         @RequestParam(required = false) Set<Long> categories,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @RequestParam(required = false, defaultValue = "0") Integer from,
                                         @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("GET /admin/events - Parameters: users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);

        List<EventFullDto> events = adminEventsService.findEvents(users, states, categories, rangeStart, rangeEnd, from, size);

        log.info("GET /admin/events - Response: {} events found", events.size());
        return events;
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable long eventId,
                                    @Validated @NotNull @RequestBody UpdateEventAdminRequest eventShortDto) {
        log.info("PATCH /admin/events/{} - Updating event with ID: {}. Update request: {}", eventId, eventId, eventShortDto);

        EventFullDto updatedEvent = adminEventsService.updateEvent(eventId, eventShortDto);

        log.info("PATCH /admin/events/{} - Event updated successfully. Updated event details: {}", eventId, updatedEvent);
        return updatedEvent;
    }
}