package com.example.dailyfix;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("fullName")
    private String name;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("password")
    private String password;

    // Constructeur pour l'inscription
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Constructeur pour la connexion
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters et Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
