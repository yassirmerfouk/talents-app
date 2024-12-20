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
    @Column(length = 5000)
    private String description;
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private SelectionStatus status;

    @ManyToOne
    private Client client;

    @OneToMany(mappedBy = "selection", cascade = CascadeType.ALL)
    private List<SelectionItem> selectionItems;

    public void copyProperties(Selection selection){
        title = selection.title;
        sector = selection.sector;
        description = selection.description;
    }

}
