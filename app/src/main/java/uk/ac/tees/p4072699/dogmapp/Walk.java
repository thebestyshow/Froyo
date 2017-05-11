package uk.ac.tees.p4072699.dogmapp;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class Walk implements Serializable{
    private double length;
    private int rating;
    private String comment,name;
    private int id;
    private int time;
    private ArrayList<LatLng> points;

    public Walk(double length, int time,ArrayList<LatLng> points) {
        this.length = length;
        this.time = time;
        this.points = points;
    }

    public Walk(String n,double length, int rating, String comment, int time) {
        this.name = n;
        this.length = length;
        this.rating = rating;
        this.comment = comment;
        this.time = time;
    }

    public Walk(String n, double length, int rating, String comment, int time, ArrayList<LatLng> points) {
        this.name = n;
        this.length = length;
        this.rating = rating;
        this.comment = comment;
        this.time = time;
        this.points = points;
    }

    public Walk(String n,double length, int rating, String comment, int id, int time) {
        this(n,length,rating,comment,time);
        this.id = id;
    }

    public Walk(String n,double length,int rating,String comment,int id,int time,ArrayList<LatLng> points){
        this(n,length,rating,comment,id,time);
        this.points = points;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public ArrayList<LatLng> getPoints() {
        return points;
    }

    public void setImage(ArrayList<LatLng> image) {
        this.points = image;
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
