package com.pulse.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "client_contracts")
@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class ClientContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contractUrl;

    @ManyToOne
    private Client client;
}
