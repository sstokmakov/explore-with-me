package ru.tokmakov.model;

import ru.tokmakov.dto.event.ApplicationStatus;
import jakarta.persistence.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String annotation;

    @NotNull
    @ManyToOne
    private Category category;

    private Integer confirmedRequests;

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

    @NotNull
    private Boolean paid;

    private Integer participantLimit;

    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private ApplicationStatus state;

    @NotNull
    private String title;

    private Integer views;
}