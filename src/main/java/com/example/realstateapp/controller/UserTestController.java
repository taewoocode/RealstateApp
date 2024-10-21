package com.example.realstateapp.controller;

import com.example.realstateapp.model.User;
import com.example.realstateapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/users")
@Controller
public class UserTestController {


    private final UserService userService;

    @Autowired
    public UserTestController(UserService userService) {
        this.userService = userService;
    }

    public String showRegistration(Model model) {
        model.addAttribute( "user", new User() );
        return "register";
    }
}
