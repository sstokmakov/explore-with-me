package ru.tokmakov.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.tokmakov.service.StatsServiceImpl;
import ru.tokmakov.dto.HitDto;
import ru.tokmakov.dto.StatsResponseDto;

import java.util.List;

@Slf4j
@RestController
public class StatsController {

    private final StatsServiceImpl statsService;

    public StatsController(StatsServiceImpl statsService) {
        this.statsService = statsService;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody HitDto hitDto) {
        log.info("save hit: {}", hitDto);
        HitDto savedHit = statsService.save(hitDto);
        log.info("Hit saved successfully: {}", savedHit);
    }

    @GetMapping("/stats")
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

    @GetMapping
    public Boolean existsByIp(@RequestParam String ip, @RequestParam String uri) {
        log.info("StatsController exists by ip is called ip={}", ip);
        Boolean res = statsService.existByIp(ip, uri);
        log.info("StatsController exists by ip returned {}", res);
        return res;
    }
}