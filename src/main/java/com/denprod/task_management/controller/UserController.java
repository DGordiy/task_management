package com.denprod.task_management.controller;

import com.denprod.task_management.entity.security.Role;
import com.denprod.task_management.entity.security.User;
import com.denprod.task_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RequestMapping("/user")
@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String showList(Model model) {
        model.addAttribute("users", repository.findAll());

        return "user_list";
    }

    @GetMapping("/new")
    public String addEntity(Model model) {
        model.addAttribute("user", new User());

        return "user_edit";
    }

    @GetMapping("/{user}")
    public String editEntity(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "user_edit";
    }

    @PostMapping
    public String saveEntity(@Valid User body, @RequestParam Map<String, String> form, BindingResult result) {
        if (result.hasErrors()) {
            return "user_edit";
        }

        User user = body.getId() == null ? new User() : repository.getById(body.getId());
        user.setUsername(body.getUsername());
        user.setEmail(body.getEmail());
        user.setPassword(passwordEncoder.encode(body.getPassword()));
        user.setEnabled(body.isEnabled());

        Set<Role> roles = new HashSet<>();
        for (Role role: Role.values()) {
            if (form.containsKey("role" + role.ordinal())) {
                roles.add(role);
            }
        }
        user.setRoles(roles);

        repository.save(user);


        return "redirect:/user";
    }

    @PostMapping("/delete")
    public String delete(@RequestBody MultiValueMap<String, String> m) {
        repository.deleteById(Integer.valueOf(m.getFirst("deleteId")));
        return "redirect:/user";
    }
}
