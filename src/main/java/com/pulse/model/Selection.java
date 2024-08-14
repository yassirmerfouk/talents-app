package com.pulse.model;

import com.pulse.enumeration.SelectionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "selections")
@AllArgsConstructor @NoArgsConstructor @Setter @Getter @Builder
public class Selection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String sector;
    private String description;
    private LocalDateTime createdAt;
    private SelectionStatus status;

    @ManyToOne
    private Client client;

    @OneToMany(mappedBy = "selection", cascade = CascadeType.ALL)
    private List<SelectionItem> selectionItems;

}
