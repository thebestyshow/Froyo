package uk.ac.tees.p4072699.dogmapp;

import java.io.Serializable;

public class Walk implements Serializable{
    private double length;
    private int rating;
    private String comment,name;
    private int id;
    private int time;



    public Walk(double length, int time) {
        this.length = length;
        this.time = time;
    }

    public Walk(String n,double length, int rating, String comment, int time) {
        this.name = n;
        this.length = length;
        this.rating = rating;
        this.comment = comment;
        this.time = time;

    }

    public Walk(String n,double length, int rating, String comment, int id, int time) {
        this.name = n;
        this.length = length;
        this.rating = rating;
        this.comment = comment;
        this.id = id;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }
}
