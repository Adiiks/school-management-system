package com.adrian.school.management.system.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("TEACHER")
@SuperBuilder
@NoArgsConstructor
public class Teacher extends User {
}
