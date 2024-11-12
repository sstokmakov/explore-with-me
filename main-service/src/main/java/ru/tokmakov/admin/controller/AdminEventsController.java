package ru.tokmakov.admin.controller;

import ru.tokmakov.dto.event.UpdateEventAdminRequest;
import ru.tokmakov.admin.service.AdminEventsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.tokmakov.dto.event.EventFullDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/admin/events")
public class AdminEventsController {
    private final AdminEventsService adminEventsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> findEvents(@RequestParam List<Integer> users,
                                         @RequestParam List<String> states,
                                         @RequestParam List<Integer> categories,
                                         @RequestParam String rangeStart,
                                         @RequestParam String rangeEnd,
                                         @RequestParam(required = false, defaultValue = "0") int from,
                                         @RequestParam(required = false, defaultValue = "10") int size) {
        return adminEventsService.findEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable long eventId,
                                    @Valid @NotNull @RequestBody UpdateEventAdminRequest eventShortDto) {
        return adminEventsService.updateEvent(eventId, eventShortDto);
    }
}