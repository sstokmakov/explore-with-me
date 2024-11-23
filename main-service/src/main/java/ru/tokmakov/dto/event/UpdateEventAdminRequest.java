package ru.tokmakov.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000)
    private String annotation;
    private Integer category;
    @Size(min = 20, max = 7000)
    private String description;
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private AdminStateAction stateAction;
    @Size(min = 3, max = 120)
    private String title;
}