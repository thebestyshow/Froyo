package uk.ac.tees.p4072699.dogmapp;

public class Walk {
    private String name;
    private double length;
    private int rating;
    private String comment;
    private int id;
    private int time;

    public Walk(double length, int time) {
        this.length = length;
        this.time = time;
    }

    public Walk(String name, double length, int rating, String comment, int time) {
        this.name = name;
        this.length = length;
        this.rating = rating;
        this.comment = comment;
        this.time = time;

    }

    public Walk(String name, double length, int rating, String comment, int id, int time) {
        this.name = name;
        this.length = length;
        this.rating = rating;
        this.comment = comment;
        this.id = id;
        this.time = time;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }
}
