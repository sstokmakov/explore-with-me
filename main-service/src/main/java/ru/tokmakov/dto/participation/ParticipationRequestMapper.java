package ru.tokmakov.dto.participation;

import lombok.experimental.UtilityClass;
import ru.tokmakov.model.ParticipationRequest;

@UtilityClass
public class ParticipationRequestMapper {
    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
        participationRequestDto.setId(participationRequest.getId());
        participationRequestDto.setCreated(participationRequest.getCreated());
        participationRequestDto.setEvent(participationRequest.getEvent().getId());
        participationRequestDto.setRequester(participationRequest.getRequester().getId());
        participationRequestDto.setStatus(participationRequest.getStatus());
        return participationRequestDto;
    }
}
