package com.example.demo.service;

import com.example.demo.models.User;
import com.example.demo.models.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UserDto;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public User save(UserDto userDto){
        User user = new User(userDto.getName(),  passwordEncoder.encode(userDto.getPassword()),  userDto.getEmail(), userDto.getRole());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(UserDto userDto){
        User user = userRepository.findByEmail(userDto.getEmail());
        if (user != null){
        user.setName(userDto.getName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        return userRepository.save(user); 
        }
        else{
            throw new RuntimeException("User not found with email: " + userDto.getEmail());
        }
        
    }

}
