package ru.tokmakov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tokmakov.dto.event.RequestStatus;
import ru.tokmakov.model.ParticipationRequest;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByEventIdAndRequesterId(Long eventId, Long requester);

    Boolean existsByRequesterIdAndEventId(Long requester, Long eventId);

    List<ParticipationRequest> findByRequesterId(Long requester);

    List<ParticipationRequest> findAllByEventIdAndStatus(Long eventId, RequestStatus status);
}
