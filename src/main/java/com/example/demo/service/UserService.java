package com.example.demo.service;

import com.example.demo.models.User;
import com.example.demo.dto.UserDto;

public interface UserService {

    User save (UserDto userDto);
    User updateUser (UserDto userDto);
    void deleteByEmail(String email);
} 