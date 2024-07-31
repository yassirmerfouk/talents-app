package com.pulse.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "confirmation_tokens")
@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;

    @ManyToOne
    private User user;
}
