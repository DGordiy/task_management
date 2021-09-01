package com.denprod.task_management.service;

import com.denprod.task_management.entity.Task;
import com.denprod.task_management.entity.security.User;
import com.denprod.task_management.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;

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
        return findPaginated(pageNumber, pageSize, sortField, sortDirection, null);
    }

    public Page<Task> findPaginated(int pageNumber, int pageSize, String sortField, String sortDirection, SearchCriteria searchCriteria) {
        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (searchCriteria == null) {
            return repository.findAll(criteriaSpecification(null, user), pageable);
        } else {
            return repository.findAll(criteriaSpecification(searchCriteria, user), pageable);
        }
    }

    private Specification<Task> criteriaSpecification(SearchCriteria searchCriteria, User user) {
        return (root, cq, cb) -> {
            if (searchCriteria == null) {
                return cb.isMember(user, root.get("users"));
            }

            Object searchValue = searchCriteria.getValue();
            Predicate predicate = null;
            switch (searchCriteria.getOperator()) {
                case EQ:
                    predicate = cb.equal(root.get(searchCriteria.getKey()), searchValue);
                    break;
                case LIKE:
                    predicate = cb.like(root.get(searchCriteria.getKey()), "%" + searchValue + "%");
                    break;
            }

            return cb.and(predicate, cb.isMember(user, root.get("users")));
        };
    }
}
