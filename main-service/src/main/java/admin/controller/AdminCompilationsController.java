package admin.controller;

import admin.service.AdminCompilationsService;
import dto.complation.CompilationDto;
import dto.complation.NewCompilationDto;
import dto.complation.UpdateCompilationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/admin/compilations")
public class AdminCompilationsController {
    private final AdminCompilationsService adminCompilationsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto saveCompilations(@Valid @NotNull @RequestBody NewCompilationDto newCompilationDto) {
        return adminCompilationsService.saveCompilations(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilations(@PathVariable long compId) {
        adminCompilationsService.deleteCompilations(compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilations(@PathVariable long compId, @Valid @NotNull @RequestBody UpdateCompilationRequest newCompilationDto) {
        return adminCompilationsService.updateCompilations(compId, newCompilationDto);
    }
}
