package ru.tokmakov.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.tokmakov.service.StatsService;
import ru.tokmakov.dto.HitDto;
import ru.tokmakov.dto.StatsResponseDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitDto save(@RequestBody HitDto hitDto) {
        log.info("save hit: {}", hitDto);
        HitDto savedHit = statsService.save(hitDto);
        log.info("Hit saved successfully: {}", savedHit);
        return savedHit;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<StatsResponseDto> getStatistics(
            @RequestParam(value = "start") String start,
            @RequestParam(value = "end") String end,
            @RequestParam(value = "uris", required = false) List<String> uris,
            @RequestParam(value = "unique", required = false, defaultValue = "false") boolean unique) {
        log.info("Fetching statistics with parameters - start: {}, end: {}, uris: {}, unique: {}", start, end, uris, unique);
        List<StatsResponseDto> stats = statsService.getStats(start, end, uris, unique);
        log.info("Statistics fetched successfully with {} records", stats.size());
        return stats;
    }
}