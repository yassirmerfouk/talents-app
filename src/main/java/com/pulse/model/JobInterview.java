package com.pulse.model;

import com.pulse.enumeration.JobInterviewStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_interviews")
@NoArgsConstructor @AllArgsConstructor @Setter @Getter @Builder
public class JobInterview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 1500)
    private String message;
    private LocalDateTime firstDate;
    private LocalDateTime secondDate;
    private LocalDateTime thirdDate;
    @Enumerated(EnumType.STRING)
    private JobInterviewStatus status;

    @ManyToOne
    private Application application;
}
