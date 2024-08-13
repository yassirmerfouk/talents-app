package com.pulse.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "selection_items")
@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class SelectionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean selected;
    private String level;
    @Column(length = 5000)
    private String report;

    @ManyToOne
    private Selection selection;

    @ManyToOne
    private Talent talent;
}
