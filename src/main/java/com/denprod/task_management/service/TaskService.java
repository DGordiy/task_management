package com.denprod.task_management.service;

import com.denprod.task_management.entity.Task;
import com.denprod.task_management.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    TaskRepository repository;

    public void save(Task task) {
        repository.save(task);
    }

    public Task getById(Integer id) {
        return repository.getById(id);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public Page<Task> findPaginated(int pageNumber, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return repository.findAll(pageable);
    }
}
