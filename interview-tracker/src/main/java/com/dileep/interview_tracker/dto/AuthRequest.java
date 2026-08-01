package com.dileep.interview_tracker.dto;

public class AuthRequest {
    private String name; // only used for register, ignored on login
    private String email;
    private String password;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}