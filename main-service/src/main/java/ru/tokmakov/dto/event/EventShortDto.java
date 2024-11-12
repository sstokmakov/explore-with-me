package ru.tokmakov.dto.event;

import ru.tokmakov.dto.category.CategoryDto;
import ru.tokmakov.dto.user.UserShortDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    @NotBlank
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    // example: 2024-12-31 15:10:05
    @NotBlank
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")
    private String eventDate;
    private Integer id;
    private UserShortDto initiator;
    @NotNull
    private Boolean paid;
    @NotBlank
    private String title;
    private Integer views;
}
