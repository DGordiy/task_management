package com.denprod.task_management.repository;

import com.denprod.task_management.entity.Task;
import com.denprod.task_management.entity.TaskComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskCommentRepository extends JpaRepository<TaskComment, Integer> {
    List<TaskComment> findAllByTaskOrderByCreatedDateDesc(Task task);
}
