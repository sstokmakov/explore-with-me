package ru.tokmakov.dto.event;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    @NotNull
    private Integer category;
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @NotNull
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")
    private String eventDate;
    @NotNull
    private Location location;
    private Boolean paid = false;
    @Min(0)
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}