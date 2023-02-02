package com.adrian.school.management.system.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("STUDENT")
@SuperBuilder
@NoArgsConstructor
public class Student extends User {

    private String parentName;
}
