package ru.tokmakov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tokmakov.dto.StatsResponseDto;
import ru.tokmakov.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Long> {
    @Query("SELECT new ru.tokmakov.dto.StatsResponseDto(h.app, h.uri, COUNT(h.ip)) " +
           "FROM Hit h " +
           "WHERE h.timestamp BETWEEN :start AND :end " +
           "AND (:uris IS NULL OR h.uri IN :uris) " +
           "GROUP BY h.app, h.uri " +
           "ORDER BY COUNT(h.ip) DESC")
    List<StatsResponseDto> findAllHits(@Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end,
                                       @Param("uris") List<String> uris);

    @Query("SELECT new ru.tokmakov.dto.StatsResponseDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
           "FROM Hit h " +
           "WHERE h.timestamp BETWEEN :start AND :end " +
           "AND (:uris IS NULL OR h.uri IN :uris) " +
           "GROUP BY h.app, h.uri " +
           "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<StatsResponseDto> findUniqueHits(@Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end,
                                          @Param("uris") List<String> uris);
}