package ru.tokmakov.controller.guest;

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
    public List<CompilationDto> findCompilations(@RequestParam Boolean pinned,
                                                 @RequestParam(required = false, defaultValue = "0") int from,
                                                 @RequestParam(required = false, defaultValue = "10") int size) {
        return guestCompilationsService.findCompilations(pinned, from, size);
    }

    @GetMapping("{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto findCompilationsByCompId(@PathVariable long compId) {
        return guestCompilationsService.findCompilationsByCompId(compId);
    }
}
