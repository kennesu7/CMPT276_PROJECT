package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;

@Controller
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/register")
    public String gerRegistrationPage(@ModelAttribute ("user") UserDto userDto){
        return "users/register";
    }
    
    
    @PostMapping("/register")
    public String saveUser(@ModelAttribute ("user") UserDto userDto, Model model)
    {
        userService.save(userDto);
        model.addAttribute("message", "Registered Successfully!");
        return "users/register";
    }
}

