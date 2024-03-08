package com.example.demo.service;

import com.example.demo.models.User;
import com.example.demo.models.UserRepository;

import org.springframework.stereotype.Service;

import com.example.demo.dto.UserDto;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    
    @Override
    public User save(UserDto userDto){
        User user = new User( userDto.getName(),  userDto.getPassword(),  userDto.getEmail(), userDto.getRole());
        return userRepository.save(user);
    }
}
