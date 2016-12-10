package com.example.conorprunty.projectsnap;

/**
 * Created by conorprunty on 10/12/2016.
 */

public class Rating {

    private int id;
    private String rating;
    private String total;

    public Rating() {
    }

    public Rating(int id, String name, String total) {
        this.id = id;
        this.rating = rating;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}