package ru.tokmakov.dto.complation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.tokmakov.dto.event.EventShortDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {
    private List<EventShortDto> events;
    @NotNull
    private Long id;
    @NotNull
    private Boolean pinned;
    @NotBlank
    private String title;
}