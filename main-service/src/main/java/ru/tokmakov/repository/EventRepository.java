package ru.tokmakov.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
           "WHERE (:users IS NULL OR e.initiator.id IN :users) " +
           "AND (:states IS NULL OR e.state IN :states) " +
           "AND (:categories IS NULL OR e.category.id IN :categories) " +
           "AND (e.eventDate >= :rangeStart) " +
           "AND (e.eventDate <= :rangeEnd)")
    List<Event> findByFilters(
            Set<Long> users,
            Set<EventState> states,
            Set<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Pageable pageable);

    @Query("SELECT e FROM Event e " +
           "WHERE (:users IS NULL OR e.initiator.id IN :users) " +
           "AND (:states IS NULL OR e.state IN :states) " +
           "AND (:categories IS NULL OR e.category.id IN :categories) " +
           "AND (e.eventDate >= :currentTimestamp)")
    List<Event> findByFiltersWithoutDate(
            Set<Long> users,
            Set<EventState> states,
            Set<Long> categories,
            LocalDateTime currentTimestamp,
            Pageable pageable);

    @Query("SELECT e FROM Event e WHERE (e.id IN :eventIds)")
    List<Event> findAllByIds(List<Long> eventIds);

    @Query("SELECT e FROM Event e " +
           "WHERE e.state = 'PUBLISHED' " +
           "AND (:text IS NULL OR (e.annotation ILIKE :text OR e.description ILIKE :text)) " +
           "AND (:categories IS NULL OR e.category.id IN :categories) " +
           "AND (:paid IS NULL OR e.paid = :paid) " +
           "AND (e.eventDate >= :start) " +
           "AND (e.eventDate <= :end) " +
           "AND (:onlyAvailable = false OR e.participantLimit = 0 OR e.participantLimit > e.confirmedRequests)")
    Page<Event> findEventsWithFilters(
            @Param("text") String text,
            @Param("categories") List<Integer> categories,
            @Param("paid") Boolean paid,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("onlyAvailable") Boolean onlyAvailable,
            Pageable pageable
    );

    @Query("SELECT e FROM Event e " +
           "WHERE e.state = 'PUBLISHED' " +
           "AND (:text IS NULL OR (e.annotation ILIKE :text OR e.description ILIKE :text)) " +
           "AND (:categories IS NULL OR e.category.id IN :categories) " +
           "AND (:paid IS NULL OR e.paid = :paid) " +
           "AND (e.eventDate >= :currentTimestamp) " +
           "AND (:onlyAvailable = false OR e.participantLimit = 0 OR e.participantLimit > e.confirmedRequests)")
    Page<Event> findEventsWithFiltersWithoutDate(
            @Param("text") String text,
            @Param("categories") List<Integer> categories,
            @Param("paid") Boolean paid,
            @Param("onlyAvailable") Boolean onlyAvailable,
            @Param("currentTimestamp") LocalDateTime currentTimestamp,
            Pageable pageable
    );
}