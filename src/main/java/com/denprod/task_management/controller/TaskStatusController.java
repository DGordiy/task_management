package com.denprod.task_management.controller;

import com.denprod.task_management.entity.TaskStatus;
import com.denprod.task_management.entity.enums.Color;
import com.denprod.task_management.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/task_status")
@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class TaskStatusController {

    @Autowired
    private TaskStatusRepository repository;

    @GetMapping
    public String showList(Model model) {
        model.addAttribute("taskStatuses", repository.findAll());

        return "task_status_list";
    }

    @GetMapping("/new")
    public String addEntity(Model model) {
        model.addAttribute("taskStatus", new TaskStatus());

        return "task_status_edit";
    }

    @GetMapping("/{id}")
    public String editEntity(@PathVariable Integer id, Model model) {
        model.addAttribute("taskStatus", repository.getById(id));

        return "task_status_edit";
    }

    @PostMapping("/save")
    public String saveEntity(@Valid TaskStatus entity, BindingResult result) {
        if (result.hasErrors()) {
            return "task_status_edit";
        }

        repository.save(entity);

        return "redirect:/task_status";
    }

    @PostMapping("/delete")
    public String delete(@RequestBody MultiValueMap<String, String> m) {
        repository.deleteById(Integer.valueOf(m.getFirst("deleteId")));
        return "redirect:/task_status";
    }

    @ModelAttribute("allColors")
    private void prepareColors(Model model) {
        model.addAttribute("allColors", Color.values());
    }
}
