package ru.tokmakov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tokmakov.model.ParticipationRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByEventId(Long eventId);

    Boolean existsByRequesterIdAndEventId(Long requester, Long eventId);

    List<ParticipationRequest> findByRequesterId(Long requester);

    Optional<ParticipationRequest> findByIdAndRequesterId(Long id, Long requester);
}
