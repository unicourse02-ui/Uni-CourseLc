package com.example.uni_courselc;

public class Universities {
    private String name;
    private int img;
    private  int ratings;

    private int stars;

    public Universities(String name, int img, int ratings, int stars){

        this.name = name;
        this.img = img;
        this.ratings = ratings;
        this.stars = stars;


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

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
