package ru.tokmakov.controller.guest;

import jakarta.servlet.http.HttpServletRequest;
import ru.tokmakov.dto.event.EventFullDto;
import ru.tokmakov.dto.event.SortType;
import ru.tokmakov.service.guest.GuestEventsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.tokmakov.dto.event.EventShortDto;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/events")
public class GuestEventsController {
    private final GuestEventsService guestEventsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> findEvents(@RequestParam(required = false) String text,
                                          @RequestParam(required = false) List<Integer> categories,
                                          @RequestParam(required = false) Boolean paid,
                                          @RequestParam(required = false) String rangeStart,
                                          @RequestParam(required = false) String rangeEnd,
                                          @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                          @RequestParam(required = false) SortType sort,
                                          @RequestParam(required = false, defaultValue = "0") int from,
                                          @RequestParam(required = false, defaultValue = "10") int size,
                                          HttpServletRequest request) {
        log.info("GET /events - Parameters: text={}, categories={}, paid={}, rangeStart={}, rangeEnd={}, onlyAvailable={}, sort={}, from={}, size={}, clientIP={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request.getRemoteAddr());

        List<EventShortDto> events = guestEventsService.findEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);

        log.info("GET /events - Response: {} events found", events.size());
        return events;
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto findEventById(@PathVariable long id, HttpServletRequest request) {
        log.info("GET /events/{} - Request received from clientIP={}", id, request.getRemoteAddr());

        EventFullDto event = guestEventsService.findEventById(id, request);

        log.info("GET /events/{} - Response: {}", id, event);
        return event;
    }
}