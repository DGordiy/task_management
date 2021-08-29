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
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq")
    @SequenceGenerator(name = "task_seq", allocationSize = 1)
    @Column(updatable = false)
    private Integer id;

    @NotBlank
    private String name;

    @NotBlank
    @Lob
    private String description;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "status_id")
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "author_id", updatable = false, nullable = false)
    @CreatedBy
    private User author;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "priority_id")
    private TaskPriority priority;

    @ManyToOne
    @JoinColumn(name = "modified_user_id")
    @LastModifiedBy
    private User modifiedBy;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    private LocalDateTime endDate;

    private LocalDateTime completeDate;

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    @PrePersist
    public void beforePersist() {
        addUsersFromThisTask();
    }

    @PreUpdate
    public void beforeUpdate() {
        checkCompletedStatus();
        addUsersFromThisTask();
    }

    private void checkCompletedStatus() {
        if (status.isCompleted()) {
            completeDate = LocalDateTime.now();
        }
    }

    private void addUsersFromThisTask() {
        if (!users.contains(assignee)) {
            users.add(assignee);
        }
        if (!users.contains(author)) {
            users.add(author);
        }
    }
}
