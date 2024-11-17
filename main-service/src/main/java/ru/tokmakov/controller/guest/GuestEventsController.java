package ru.tokmakov.controller.guest;

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
    public List<EventShortDto> findEvents(@RequestParam String text,
                                          @RequestParam List<Integer> categories,
                                          @RequestParam Boolean paid,
                                          @RequestParam(required = false) String rangeStart,
                                          @RequestParam(required = false) String rangeEnd,
                                          @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                          @RequestParam SortType sort,
                                          @RequestParam(required = false, defaultValue = "0") int from,
                                          @RequestParam(required = false, defaultValue = "10") int size) {
        return guestEventsService.findEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto findEventById(@PathVariable long id) {
        return guestEventsService.findEventById(id);
    }
}