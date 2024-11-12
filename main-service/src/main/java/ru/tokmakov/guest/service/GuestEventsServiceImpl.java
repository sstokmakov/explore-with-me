package ru.tokmakov.guest.service;

import ru.tokmakov.dto.event.SortType;
import org.springframework.stereotype.Service;
import ru.tokmakov.dto.event.EventShortDto;

import java.util.List;

@Service
public class GuestEventsServiceImpl implements GuestEventsService {
    @Override
    public EventShortDto findEvents(String text, List<Integer> categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, SortType sort, int from, int size) {
        return null;
    }

    @Override
    public EventShortDto findEventById(long id) {
        return null;
    }
}
