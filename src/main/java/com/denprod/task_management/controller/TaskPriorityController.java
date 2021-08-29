package com.denprod.task_management.controller;

import com.denprod.task_management.entity.TaskPriority;
import com.denprod.task_management.entity.enums.Color;
import com.denprod.task_management.repository.TaskPriorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/task_priority")
@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class TaskPriorityController {

    @Autowired
    private TaskPriorityRepository repository;

    @GetMapping
    public String showList(Model model) {
        model.addAttribute("taskPriorities", repository.findAll());

        return "task_priority_list";
    }

    @GetMapping("/new")
    public String newEntity(Model model) {
        model.addAttribute("taskPriority", new TaskPriority());

        return "task_priority_edit";
    }

    @GetMapping("/{id}")
    public String editEntity(@PathVariable Integer id, Model model) {
        model.addAttribute("taskPriority", repository.getById(id));

        return "task_priority_edit";
    }

    @PostMapping("/save")
    public String saveEntity(@Valid TaskPriority entity, BindingResult result) {
        if (result.hasErrors()) {
            return "task_priority_edit";
        }

        repository.save(entity);

        return "redirect:/task_priority";
    }

    @PostMapping("/delete")
    public String delete(@RequestBody MultiValueMap<String, String> m) {
        repository.deleteById(Integer.valueOf(m.getFirst("deleteId")));
        return "redirect:/task_priority";
    }

    @ModelAttribute("allColors")
    private void prepareColors(Model model) {
        model.addAttribute("allColors", Color.values());
    }
}
