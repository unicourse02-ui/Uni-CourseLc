package com.example.uni_courselc;

public class University {

    private String name;
    private String location;
    private String type;
    private int imageUrl;

    public University(String name, String location, String type, int imageUrl) {
        this.name = name;
        this.location = location;
        this.type = type;
        this.imageUrl = imageUrl;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
