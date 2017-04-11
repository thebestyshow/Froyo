package uk.ac.tees.p4072699.dogmapp;

import android.media.Image;

public class Dog {

    private String name;
    private String owner;
    private Image img;
    private int totwalks;
    private int totdistance;
    private int id;

    public int getTotwalks() {
        return totwalks;
    }

    public Dog(String name, String owner, int id) {
        this.name = name;
        this.owner = owner;
        this.id = id;
    }

    public Dog(String name, String owner) {
        this.name = name;
        this.owner = owner;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }
}
