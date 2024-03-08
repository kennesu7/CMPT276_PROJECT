package com.example.demo.models;

//import org.hibernate.mapping.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//@Repository <- used to be here
public interface UserRepository extends JpaRepository<User, Integer>{
    List<User> findByEmail(String email);
    List<User> findByStringAndPassword(String name, String password); // < -- causing error if string gettings/setters are not present in user,java
    
} 

