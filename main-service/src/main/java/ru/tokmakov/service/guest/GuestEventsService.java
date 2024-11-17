package ru.tokmakov.service.guest;

import ru.tokmakov.dto.event.SortType;
import ru.tokmakov.dto.event.EventShortDto;

import java.util.List;

public interface GuestEventsService {
    EventShortDto findEvents(String text, List<Integer> categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, SortType sort, int from, int size);

    EventShortDto findEventById(long id);
}
