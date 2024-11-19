package ru.tokmakov.service.admin;

import ru.tokmakov.dto.event.EventState;
import ru.tokmakov.dto.event.UpdateEventAdminRequest;
import ru.tokmakov.dto.event.EventFullDto;

import java.util.List;
import java.util.Set;

public interface AdminEventsService {
    List<EventFullDto> findEvents(Set<Long> users,
                                  Set<EventState> states,
                                  Set<Long> categories,
                                  String rangeStart,
                                  String rangeEnd,
                                  Integer from,
                                  Integer size);

    EventFullDto updateEvent(long eventId, UpdateEventAdminRequest eventShortDto);
}
