package uk.ac.tees.p4072699.dogmapp;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Dog implements Serializable {

    private String name;
    private int ownerID;
    private Image img;
    private int totwalks;
    private double totdistance;
    private int id;



    public Dog(int id,String name, int owner,int totWalks, double totdistance) {
        this(name,owner);
        this.totwalks = totWalks;
        this.totdistance = totdistance;
        this.id = id;
    }

    public Dog(int id,String name, int owner) {
        this(name,owner);
        this.id = id;
        totwalks = 0;
        totdistance = 0.0;
    }

    public Dog(String name, int owner) {
        this.name = name;
        this.ownerID = owner;
    }


    public int getTotwalks() {
        return totwalks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTotwalks(int totwalks) {
        this.totwalks = totwalks;
    }

    public double getTotdistance() {
        return totdistance;
    }

    public void setTotdistance(double totdistance) {
        this.totdistance = totdistance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwner(int owner) {
        this.ownerID = owner;
    }

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

}
