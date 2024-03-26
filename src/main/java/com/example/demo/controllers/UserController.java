package com.example.demo.controllers;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.example.demo.dto.UserDto;
import com.example.demo.models.User;
import com.example.demo.models.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.models.ItineraryRepository;
import com.example.demo.models.Itinerary;






@Controller
public class UserController {
    
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ItineraryRepository itineraryRepo;

    @GetMapping("/home")
    public String home()
    {
        return "home";
    }
    
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
public String userPage (Model model, Principal principal) { //@RequestParam(name = "uid") int uid,
    UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    model.addAttribute("user", userDetails);

    List<Itinerary> itineraries = itineraryRepo.findAllByEmail(principal.getName());
    model.addAttribute("itinerary", itineraries);

    return "user";
}


	@GetMapping("admin-page")
	public String adminPage (Model model, Principal principal) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);

        List<User> users = userRepo.findAll();
        model.addAttribute("users", users);
		return "admin";
	}
    
    

}
