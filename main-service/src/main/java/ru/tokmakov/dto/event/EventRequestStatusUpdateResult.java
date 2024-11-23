package ru.tokmakov.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.tokmakov.dto.participation.ParticipationRequestDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}
