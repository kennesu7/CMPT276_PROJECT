package com.example.demo.controllers;

import java.security.Principal;

import org.h2.engine.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;

import org.springframework.security.core.Authentication;






@Controller
public class UserController {
    
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;
    
    @GetMapping("/register")
    public String getRegistrationPage(@ModelAttribute ("user") UserDto userDto){
        return "register";
    }
    
    
    @PostMapping("/register")
    public String saveUser(@ModelAttribute ("user") UserDto userDto, Model model)
    {
        userService.save(userDto);
        model.addAttribute("message", "Registered Successfully!");
        return "registerSuccess";
    }

    @GetMapping("/login")
    public String login()
    {
        return "login";
    }

	@GetMapping("user-page")
	public String userPage (Model model, Principal principal) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		return "user";
	}
	
	@GetMapping("admin-page")
	public String adminPage (Model model, Principal principal) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		return "admin";
	}
    
    

    


}