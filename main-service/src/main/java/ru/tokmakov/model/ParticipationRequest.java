package ru.tokmakov.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.tokmakov.dto.event.RequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "participations")
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    private User requester;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}