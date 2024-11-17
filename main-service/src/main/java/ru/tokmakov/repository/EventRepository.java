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

    @Query("SELECT e FROM Event e " +
           "WHERE e.state = 'PUBLISHED' " +
           "AND (e.annotation ILIKE CONCAT('%', :text, '%') " +
           "OR (e.description ILIKE CONCAT('%', :text, '%'))) " +
           "AND (e.category.id IN :categories) " +
           "AND (e.paid = :paid) " +
           "AND (e.eventDate >= :start) " +
           "AND (e.eventDate <= :end) " +
           "AND ((:onlyAvailable = false) OR (e.participantLimit > e.confirmedRequests)) " +
           "ORDER BY e.eventDate")
    Page<Event> findEventsWithFiltersOrderByDate(
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
           "AND (e.annotation ILIKE CONCAT('%', :text, '%') " +
           "OR (e.description ILIKE CONCAT('%', :text, '%'))) " +
           "AND (e.category.id IN :categories) " +
           "AND (e.paid = :paid) " +
           "AND (e.eventDate >= :start) " +
           "AND (e.eventDate <= :end) " +
           "AND ((:onlyAvailable = false) OR (e.participantLimit > e.confirmedRequests)) " +
           "ORDER BY e.views")
    Page<Event> findEventsWithFiltersOrderByViews(
            @Param("text") String text,
            @Param("categories") List<Integer> categories,
            @Param("paid") Boolean paid,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("onlyAvailable") Boolean onlyAvailable,
            Pageable pageable
    );

}