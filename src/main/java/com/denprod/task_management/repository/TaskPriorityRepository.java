package com.denprod.task_management.repository;

import com.denprod.task_management.entity.TaskPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskPriorityRepository extends JpaRepository<TaskPriority, Integer> {
}
