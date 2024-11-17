package ru.tokmakov.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.tokmakov.dto.event.EventState;
import ru.tokmakov.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByInitiatorId(Long initiatorId, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long initiatorId);

    Boolean existsByCategoryId(Long categoryId);

    @Query("SELECT e FROM Event e " +
           "WHERE (e.initiator.id IN :users) " +
           "AND (e.state IN :states) " +
           "AND (e.category.id IN :categories) " +
           "AND (e.eventDate >= :rangeStart) " +
           "AND (e.eventDate <= :rangeEnd)")
    List<Event> findByFilters(
            Set<Long> users,
            Set<EventState> states,
            Set<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Pageable pageable);

    @Query("SELECT e FROM Event e WHERE (e.id IN :eventIds)")
    Set<Event> findAllByIds(Set<Long> eventIds);
}