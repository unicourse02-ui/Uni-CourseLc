package com.example.uni_courselc;

public class HelperClass {
    private String name;
    private String username;
    private String password;

    private  String id;

    public HelperClass() {
        // Default constructor required for Firebase
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HelperClass(String name, String username, String password, String id) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}