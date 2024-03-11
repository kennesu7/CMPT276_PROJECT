package com.example.demo.service;

import com.example.demo.models.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.*;
import  org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.models.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {
	
	 @Autowired
	 private UserRepository userRepository;

	@Override
	//uses repository to find the username
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("user not found");
		}
		
		return new CustomUserDetail(user);

	}

}