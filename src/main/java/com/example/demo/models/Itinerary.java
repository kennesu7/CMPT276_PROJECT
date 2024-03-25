package com.example.demo.models;

import jakarta.persistence.*;

@Entity
//no need for @Table annotation since the table name is the same as the class name
public class Itinerary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long uid;
    
    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;
    
    private String itinerary; // The generated itinerary string

    // Constructors, getters, and setters
    public Itinerary() {}

    public Long getId() {
        return uid;
    }

    public void setId(Long uid) {
        this.uid = uid;
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