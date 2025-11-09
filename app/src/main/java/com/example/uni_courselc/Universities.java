package com.example.uni_courselc;

public class Universities {
    private String name;
    private String img;
    private  int ratings;

    private int stars;

    private String about;

    private  String ApplicationLink;

    private String Contact;

    private  String Address;


    public Universities(String name, String img, int ratings, int stars,String about,String ApplicationLink,String Contact,String Address){

        this.name = name;
        this.img = img;
        this.ratings = ratings;
        this.stars = stars;
        this.about = about;
        this.ApplicationLink = ApplicationLink;
        this.Contact = Contact;
        this.Address= Address;



    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getApplicationLink() {
        return ApplicationLink;
    }

    public void setApplicationLink(String applicationLink) {
        ApplicationLink = applicationLink;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
