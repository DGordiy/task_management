package com.denprod.task_management.entity;

import com.denprod.task_management.entity.security.User;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_seq")
    @SequenceGenerator(name = "project_seq", allocationSize = 1)
    @Column(updatable = false)
    private Integer id;

    @NotBlank
    private String name;

    @NotBlank
    @Lob
    private String description;

    @ManyToOne
    @JoinColumn(name = "author_id", updatable = false, nullable = false)
    @CreatedBy
    private User author;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "modified_user_id")
    @LastModifiedBy
    private User modifiedBy;

    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
