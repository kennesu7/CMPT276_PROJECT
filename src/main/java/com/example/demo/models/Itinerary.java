package com.example.demo.models;

import jakarta.persistence.*;

@Entity
//no need for @Table annotation since the table name is the same as the class name
public class Itinerary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int uid;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String email;
    
    @Column(name="itinerary", length=1000000000)
    private String itinerary; // The generated itinerary string

    // Constructors, getters, and setters
    public Itinerary() {}

    public int getUid() {
        return uid;
    }

    public void setId(int uid) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setItinerary(String itinerary) {
        this.itinerary = itinerary;
    }

    public Itinerary(User user, String email, String itinerary) {
        this.user = user;
        this.email = email;
        this.itinerary = itinerary;
    }
}