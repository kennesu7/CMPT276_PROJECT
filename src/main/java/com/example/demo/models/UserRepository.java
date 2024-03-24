package com.example.demo.models;

//import org.hibernate.mapping.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository 
public interface UserRepository extends JpaRepository<User, Integer>{
    User findByEmail(String email);
    List<User> findByName(String name);
    List<User> findByNameAndPassword(String name, String password);
} 

