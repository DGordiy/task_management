package com.denprod.task_management.entity;

import com.denprod.task_management.entity.enums.Color;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class TaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_status_seq")
    @SequenceGenerator(name = "task_status_seq", allocationSize = 1)
    @Column(updatable = false)
    private Integer id;

    @NotBlank
    private String name;

    private boolean completed;

    @Enumerated(EnumType.STRING)
    private Color color = Color.DARK;

}
