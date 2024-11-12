package guest.controller;

import dto.event.SortType;
import guest.service.GuestEventsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import dto.event.EventShortDto;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/events")
public class GuestEventsController {
    private final GuestEventsService guestEventsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public EventShortDto findEvents(@RequestParam String text,
                                    @RequestParam List<Integer> categories,
                                    @RequestParam Boolean paid,
                                    @RequestParam String rangeStart,
                                    @RequestParam String rangeEnd,
                                    @RequestParam Boolean onlyAvailable,
                                    @RequestParam SortType sort,
                                    @RequestParam(required = false, defaultValue = "0") int from,
                                    @RequestParam(required = false, defaultValue = "10") int size) {
        return guestEventsService.findEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventShortDto findEventById(@PathVariable long id) {
        return guestEventsService.findEventById(id);
    }
}