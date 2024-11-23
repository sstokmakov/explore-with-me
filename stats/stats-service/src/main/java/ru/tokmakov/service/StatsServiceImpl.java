package ru.tokmakov.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tokmakov.dto.HitDto;
import ru.tokmakov.dto.StatsResponseDto;
import ru.tokmakov.mapper.HitMapper;
import ru.tokmakov.model.Hit;
import ru.tokmakov.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Transactional
    public HitDto save(HitDto hitDto) {
        log.info("Saving hit: {}", hitDto);
        Hit hit = HitMapper.hitDtoToHit(hitDto);
        Hit savedHit = statsRepository.save(hit);
        HitDto savedHitDto = HitMapper.hitToHitDto(savedHit);
        log.info("Hit saved successfully: {}", savedHitDto);
        return savedHitDto;
    }

    @Transactional(readOnly = true)
    public List<StatsResponseDto> getStats(String start, String end, List<String> uris, boolean unique) {
        log.info("Fetching stats with parameters - start: {}, end: {}, uris: {}, unique: {}", start, end, uris, unique);

        LocalDateTime startDateTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDateTime = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("Start date and end date must be before end date");
        }

        List<StatsResponseDto> stats;
        if (unique) {
            log.debug("Fetching unique hits");
            stats = statsRepository.findUniqueHits(startDateTime, endDateTime, uris);
        } else {
            log.debug("Fetching all hits");
            stats = statsRepository.findAllHits(startDateTime, endDateTime, uris);
        }

        log.info("Statistics fetched successfully with {} records", stats.size());
        return stats;
    }

    public Boolean existByIp(String ip, String uri) {
        return statsRepository.existsByIpAndUri(ip, uri);
    }
}