package ru.tokmakov.controller.guest;

import jakarta.validation.constraints.Min;
import ru.tokmakov.dto.complation.CompilationDto;
import ru.tokmakov.service.guest.GuestCompilationsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/compilations")
public class GuestCompilationsController {
    private final GuestCompilationsService guestCompilationsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> findCompilations(@RequestParam(required = false) Boolean pinned,
                                                 @Min(value = 0, message = "The 'from' parameter must be 0 or greater")
                                                 @RequestParam(required = false, defaultValue = "0") Integer from,
                                                 @Min(value = 1, message = "The 'size' parameter must be 1 or greater")
                                                 @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("GET /compilations - Fetching compilations with parameters: pinned={}, from={}, size={}", pinned, from, size);

        List<CompilationDto> compilations = guestCompilationsService.findCompilations(pinned, from, size);

        log.info("GET /compilations - Found {} compilations", compilations.size());
        return compilations;
    }

    @GetMapping("{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto findCompilationsByCompId(@PathVariable Long compId) {
        log.info("GET /compilations/{} - Fetching compilation details for compId={}", compId, compId);

        CompilationDto compilation = guestCompilationsService.findCompilationsByCompId(compId);

        log.info("GET /compilations/{} - Compilation details fetched: {}", compId, compilation);
        return compilation;
    }
}
