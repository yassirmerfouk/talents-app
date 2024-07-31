package com.pulse.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "languages")
@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String level;

    @ManyToOne
    private Talent talent;

    public void copyProperties(Language language){
        title = language.title;
        level = language.level;
    }
}
