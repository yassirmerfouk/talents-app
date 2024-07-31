package com.pulse.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_tokens")
@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class PasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;

    @ManyToOne
    private User user;
}
