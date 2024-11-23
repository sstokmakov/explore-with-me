package ru.tokmakov.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.tokmakov.service.admin.AdminCompilationsService;
import ru.tokmakov.dto.complation.CompilationDto;
import ru.tokmakov.dto.complation.NewCompilationDto;
import ru.tokmakov.dto.complation.UpdateCompilationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/admin/compilations")
public class AdminCompilationsController {
    private final AdminCompilationsService adminCompilationsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto saveCompilations(@Validated @NotNull @RequestBody NewCompilationDto newCompilationDto) {
        log.info("POST /admin/compilations - Request body: {}", newCompilationDto);

        CompilationDto result = adminCompilationsService.saveCompilations(newCompilationDto);

        log.info("POST /admin/compilations - Response: {}", result);
        return result;
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilations(@PathVariable Long compId) {
        log.info("DELETE /admin/compilations/{} - Request received", compId);

        adminCompilationsService.deleteCompilations(compId);

        log.info("DELETE /admin/compilations/{} - Compilation deleted", compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilations(@PathVariable Long compId, @Valid @NotNull @RequestBody UpdateCompilationRequest newCompilationDto) {
        log.info("PATCH /admin/compilations/{} - Request body: {}", compId, newCompilationDto);

        CompilationDto result = adminCompilationsService.updateCompilations(compId, newCompilationDto);

        log.info("PATCH /admin/compilations/{} - Response: {}", compId, result);
        return result;
    }
}