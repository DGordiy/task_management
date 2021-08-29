package com.denprod.task_management.entity;

import com.denprod.task_management.entity.enums.Color;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class TaskPriority {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_priority_seq")
    @SequenceGenerator(name = "task_priority_seq", allocationSize = 1)
    @Column(updatable = false)
    private Integer id;

    @NotBlank
    private String name;

    @Max(9)
    @Column(length = 1)
    private int value;

    @Enumerated(EnumType.STRING)
    private Color color = Color.DARK;

    public String getStringColor() {
        return color.toString();
    }
}
