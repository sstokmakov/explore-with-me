package admin.service;

import dto.event.UpdateEventAdminRequest;
import dto.event.EventFullDto;

import java.util.List;

public interface AdminEventsService {
    List<EventFullDto> findEvents(List<Integer> users,
                                  List<String> states,
                                  List<Integer> categories,
                                  String rangeStart,
                                  String rangeEnd,
                                  int from,
                                  int size);

    EventFullDto updateEvent(long eventId, UpdateEventAdminRequest eventShortDto);
}
