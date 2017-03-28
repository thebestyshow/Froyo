package uk.ac.tees.p4072699.dogmapp;

/**
 * Created by p4061644 on 07/03/2017.
 */

public class Walk {
    private String name;
    private String length;
    private int rating;
    private String comment;

    public Walk(String name, String length, int rating, String comment) {
        this.name = name;
        this.length = length;
        this.rating = rating;
        this.comment = comment;
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
