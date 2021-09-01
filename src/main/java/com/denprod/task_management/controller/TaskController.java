package com.denprod.task_management.controller;

import com.denprod.task_management.entity.Task;
import com.denprod.task_management.entity.TaskComment;
import com.denprod.task_management.entity.security.User;
import com.denprod.task_management.repository.*;
import com.denprod.task_management.service.SearchCriteria;
import com.denprod.task_management.service.SearchOperator;
import com.denprod.task_management.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RequestMapping("/task")
@Controller
public class TaskController {

    private final static int PAGE_SIZE = 10;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskPriorityRepository taskPriorityRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskCommentRepository taskCommentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping
    public String showList(@RequestParam(name = "page", required = false) Integer pageNumber,
                           @RequestParam(name = "sort_by", required = false) String sortBy,
                           @RequestParam(name = "sort_dir", required = false) String sortDir,
                           @RequestParam(name = "filter_name", required = false) String filterName,
                           @RequestParam(name = "filter_value", required = false) String filterValue,
                           Model model) {

        if (pageNumber == null) {
            pageNumber = 1;
        }

        if (sortBy == null) {
            sortBy = "id";
        }

        if (sortDir == null) {
            sortDir = "asc";
        }

        if (filterName == null) {
            filterName = "id";
        }

        Object searchVal = null;
        if (filterValue != null && !filterValue.isBlank()) {
            switch (filterName) {
                case "id":
                    searchVal = Integer.valueOf(filterValue);
                    break;
                case "author":
                case "assignee":
                    searchVal = userRepository.getById(Integer.valueOf(filterValue));
                    break;
                case "project":
                    searchVal = projectRepository.getById(Integer.valueOf(filterValue));
                    break;
                default:
                    searchVal = filterValue.trim();
            }
        }

        Page<Task> pages;
        if (searchVal == null
                || searchVal instanceof Integer && searchVal.equals(0)
                || searchVal instanceof String && ((String) searchVal).isBlank()) {
            pages = taskService.findPaginated(pageNumber, PAGE_SIZE, sortBy, sortDir);
        } else {
            pages = taskService.findPaginated(pageNumber, PAGE_SIZE, sortBy, sortDir, new SearchCriteria(filterName, searchVal, searchVal instanceof String ? SearchOperator.LIKE : SearchOperator.EQ));
        }
        List<Task> taskList = pages.getContent();

        model.addAttribute("tasks", taskList);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalItems", pages.getTotalElements());
        model.addAttribute("totalPages", pages.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("filterName", filterName);
        model.addAttribute("filterValue", filterValue);

        return "task_list";
    }

    @GetMapping("/new")
    public String addEntity(Model model) {
        Task task = new Task();
        model.addAttribute("task", task);

        return "task_edit";
    }

    @GetMapping("/{task}")
    public String editEntity(@PathVariable Task task, Model model) {
        model.addAttribute("task", task);

        return "task_edit";
    }

    @GetMapping("/view")
    public String showEntity(@RequestParam Integer id, Model model) {
        Task task = taskService.getById(id);
        model.addAttribute("task", task);
        model.addAttribute("taskComments", taskCommentRepository.findAllByTaskOrderByCreatedDateDesc(task));

        TaskComment taskComment = new TaskComment();
        taskComment.setTask(task);
        model.addAttribute("comment", taskComment);

        return "task_view";
    }

    @PostMapping("/save")
    public String saveEntity(@Valid Task entity, BindingResult result, @RequestParam Map<String, String> form) {
        try {
            Set<User> users = entity.getUsers();
            users.clear();
            for (User user: userRepository.findAll()) {
                if (form.containsKey("user" + user.getId())) {
                    users.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result.hasErrors()) {
            return "task_edit";
        }

        entity.correctEmptyFields();
        taskService.save(entity);

        return "redirect:/task";
    }

    @PostMapping("/save_from_view")
    public String saveEntityFromView(@Valid Task entity, BindingResult result) {
        if (result.hasErrors()) {
            return "task_view";
        }

        entity.correctEmptyFields();
        Task task = taskService.getById(entity.getId());
        entity.setUsers(task.getUsers());
        taskService.save(entity);

        return "redirect:/task/view?id=" + entity.getId();
    }

    @PostMapping("/delete")
    public String delete(@RequestBody MultiValueMap<String, String> m) {
        taskService.deleteById(Integer.valueOf(m.getFirst("deleteId")));
        return "redirect:/task";
    }

    //Working with comments for task
    @PostMapping("/add_comment")
    public String addComment(@Valid TaskComment body) {
        taskCommentRepository.save(body);

        return "redirect:/task/view?id="+body.getTask().getId();
    }

    @PostMapping("/save_comment")
    public String saveComment(@RequestBody MultiValueMap<String, String> m) {
        Integer id = Integer.valueOf(m.getFirst("id"));
        TaskComment taskComment = taskCommentRepository.getById(id);
        taskComment.setDescription(m.getFirst("description"));
        taskCommentRepository.save(taskComment);

        return "redirect:/task/view?id="+taskComment.getTask().getId();
    }

    @PostMapping("/delete_comment")
    public String deleteComment(@RequestBody MultiValueMap<String, String> m) {
        TaskComment comment = taskCommentRepository.getById(Integer.valueOf(m.getFirst("deleteId")));
        Task task = comment.getTask();
        taskCommentRepository.delete(comment);
        return "redirect:/task/view?id="+task.getId();
    }

    @ModelAttribute("taskPriorities")
    private void prepareTaskPriorities(Model model) {
        model.addAttribute("taskPriorities", taskPriorityRepository.findAll());
    }

    @ModelAttribute("taskStatuses")
    private void prepareTaskStatuses(Model model) {
        model.addAttribute("taskStatuses", taskStatusRepository.findAll());
    }

    @ModelAttribute("allUsers")
    private void prepareUsers(Model model) {
        model.addAttribute("allUsers", userRepository.findAll());
    }

    @ModelAttribute("projects")
    private void prepareProjects(Model model) {
        model.addAttribute("projects", projectRepository.findAll());
    }

    @ModelAttribute("filterFields")
    private void prepareFilterFields(Model model) {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("project", "Project");
        fields.put("assignee", "Assignee");
        fields.put("author", "Author");
        fields.put("id", "Id");
        fields.put("name", "Name");
        model.addAttribute("filterFields", fields);
    }
}
