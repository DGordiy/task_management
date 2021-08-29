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
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class TaskComment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_comment_seq")
    @SequenceGenerator(name = "task_comment_seq", allocationSize = 1)
    @Column(updatable = false)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "author_id", updatable = false, nullable = false)
    @CreatedBy
    private User author;

    @NotBlank
    @Lob
    private String description;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name = "modified_user_id")
    @LastModifiedBy
    private User modifiedBy;
}
