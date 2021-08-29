package com.denprod.task_management.controller;

import com.denprod.task_management.entity.Project;
import com.denprod.task_management.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/project")
@Controller
public class ProjectController {

    @Autowired
    private ProjectRepository repository;

    @GetMapping
    public String showList(Model model) {
        model.addAttribute("projects", repository.findAll());

        return "project_list";
    }

    @GetMapping("/new")
    public String newEntity(Model model) {
        model.addAttribute("project", new Project());

        return "project_edit";
    }

    @GetMapping("/{id}")
    public String editEntity(@PathVariable Integer id, Model model) {
        model.addAttribute("project", repository.getById(id));

        return "project_edit";
    }

    @PostMapping("/save")
    public String saveEntity(@Valid Project entity, BindingResult result) {
        if (result.hasErrors()) {
            return "project_edit";
        }

        repository.save(entity);

        return "redirect:/project";
    }

    @PostMapping("/delete")
    public String delete(@RequestBody MultiValueMap<String, String> m) {
        repository.deleteById(Integer.valueOf(m.getFirst("deleteId")));
        return "redirect:/project";
    }

}
