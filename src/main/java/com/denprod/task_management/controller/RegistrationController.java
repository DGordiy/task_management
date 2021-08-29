package com.denprod.task_management.controller;

import com.denprod.task_management.entity.security.User;
import com.denprod.task_management.entity.security.UserService;
import com.denprod.task_management.service.UserDAOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private UserDAOService userDAOService;

    @GetMapping
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "security/registration";
    }

    @PostMapping
    public String addUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "security/registration";
        }

        if (!userDAOService.addUser(user)) {
            model.addAttribute("message", "User with this name already exists");
            return "security/registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code, Model model) {
        boolean isActivated = userDAOService.activateUser(code);

        if (isActivated) {
            model.addAttribute("message", "User was successfully activated");
        } else {
            model.addAttribute("message", "Activation code not found");
        }

        return "login";
    }
}
