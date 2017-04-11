package uk.ac.tees.p4072699.dogmapp;

import android.media.Image;

public class Dog {

    private String name;
    private int ownerID;
    private Image img;
    private int totwalks;
    private int totdistance;
    private int id;

    public int getTotwalks() {
        return totwalks;
    }

    public Dog(String name, int owner, int id) {
        this.name = name;
        this.ownerID = owner;
        this.id = id;
    }

    public Dog(String name, int owner) {
        this.name = name;
        this.ownerID = owner;
        totwalks = 5;
        totdistance = 27;

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

    public int getTotdistance() {
        return totdistance;
    }

    public void setTotdistance(int totdistance) {
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
