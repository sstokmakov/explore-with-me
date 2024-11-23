package ru.tokmakov.service.guest;

import jakarta.servlet.http.HttpServletRequest;
import ru.tokmakov.dto.event.EventFullDto;
import ru.tokmakov.dto.event.SortType;
import ru.tokmakov.dto.event.EventShortDto;

import java.util.List;

public interface GuestEventsService {
    List<EventShortDto> findEvents(String text, List<Integer> categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, SortType sort, Integer from, Integer size, HttpServletRequest request);

    EventFullDto findEventById(long id, HttpServletRequest request);
}
