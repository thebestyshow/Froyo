package uk.ac.tees.p4072699.dogmapp;

/**
 * Created by p4061644 on 07/03/2017.
 */

public class Walk {
    private String name;
    private String length;
    private int rating;

    Walk(String n, String l, int r){
        this.name = n;
        this.length = l;
        this.rating = r;
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
