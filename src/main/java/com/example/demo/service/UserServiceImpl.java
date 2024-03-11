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
        User user = new User( userDto.getName(),  passwordEncoder.encode(userDto.getPassword()),  userDto.getEmail(), userDto.getRole());
        return userRepository.save(user);
    }
}
