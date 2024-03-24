package com.example.demo.Controllers;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.controllers.UserController;
import com.example.demo.models.User;
import com.example.demo.models.UserRepository;

import static org.mockito.Mockito.when;
import org.hamcrest.Matchers;

import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;


@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserRepository userRepository;
    
    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("null")
    @Test
    void testGetAllUsers() throws Exception {
    User u1 = new User();
        u1.setName("bobby");
        u1.setEmail("bobby@sfu.com");
        u1.setPassword("1234");
        

        User u2 = new User();
        u2.setName("jane");
        u1.setEmail("jane@sfu.com");
        u2.setPassword("abcd");
        

        List<User> user = new ArrayList<User>();
        user.add(u1);
        user.add(u2);

        when(userRepository.findAll()).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/all"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("users/showAll"))
            
            .andExpect(MockMvcResultMatchers.model().attribute("usrs", hasItem(
                allOf(
                    hasProperty("name", Matchers.is("bobby")),
                    hasProperty("password", Matchers.is("1234")),
                    hasProperty("email", Matchers.is("bobby@sfu.com"))
                )
            )));
    }
}
