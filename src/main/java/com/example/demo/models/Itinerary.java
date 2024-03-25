package com.example.demo.models;

import jakarta.persistence.*;

@Entity
//no need for @Table annotation since the table name is the same as the class name
public class Itinerary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name="itinerary")
    private String itinerary; // The generated itinerary string

    // Constructors, getters, and setters
    public Itinerary() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer uid) {
        this.id = uid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getItinerary() {
        return itinerary;
    }

    public void setItinerary(String itinerary) {
        this.itinerary = itinerary;
    }

    public Itinerary(User user, String itinerary) {
        this.user = user;
        this.itinerary = itinerary;
    }
}