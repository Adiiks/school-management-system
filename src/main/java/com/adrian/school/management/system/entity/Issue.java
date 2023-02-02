package com.adrian.school.management.system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String type;
    private String details;
    private boolean resolved;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
}
