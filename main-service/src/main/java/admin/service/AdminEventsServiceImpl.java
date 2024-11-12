package admin.service;

import dto.event.UpdateEventAdminRequest;
import org.springframework.stereotype.Service;
import dto.event.EventFullDto;

import java.util.List;

@Service
public class AdminEventsServiceImpl implements AdminEventsService {
    @Override
    public List<EventFullDto> findEvents(List<Integer> users, List<String> states, List<Integer> categories, String rangeStart, String rangeEnd, int from, int size) {
        return List.of();
    }

    @Override
    public EventFullDto updateEvent(long eventId, UpdateEventAdminRequest eventShortDto) {
        return null;
    }
}
