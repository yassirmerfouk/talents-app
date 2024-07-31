package com.pulse.model;

import com.pulse.enumeration.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String body;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    private Long relatedModel;
    private boolean seen;
    private boolean clicked;
    private LocalDateTime createdAt;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

}
