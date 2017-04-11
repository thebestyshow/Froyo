package uk.ac.tees.p4072699.dogmapp;

public class Walk {
    private String name;
    private String length;
    private int rating;
    private String comment;
    private int id;

    public Walk(String name, String length, int rating, String comment, int id) {
        this.name = name;
        this.length = length;
        this.rating = rating;
        this.comment = comment;
        this.id = id;
    }

    public Walk(String name, String length, int rating, String comment) {
        this.name = name;
        this.length = length;
        this.rating = rating;
        this.comment = comment;
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

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}
