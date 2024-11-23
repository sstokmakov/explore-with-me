package ru.tokmakov.model;

import lombok.Getter;
import lombok.Setter;
import ru.tokmakov.dto.event.EventState;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String annotation;

    @NotNull
    @ManyToOne
    private Category category;

    private int confirmedRequests;

    private LocalDateTime createdOn;

    private String description;

    @NotNull
    private LocalDateTime eventDate;

    @NotNull
    @ManyToOne
    private User initiator;

    @NotNull
    private Double lat;

    @NotNull
    private Double lon;

    private Boolean paid;

    private int participantLimit;

    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @NotNull
    private String title;

    private int views;
}