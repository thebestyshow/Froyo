package uk.ac.tees.p4072699.dogmapp;

import android.media.Image;

import java.io.Serializable;
import java.util.Date;

public class Owner implements Serializable {
    private int id, tot_walks;
    private double tot_dis;
    private String name;
    private String email;
    private String Password;
    private Date dob;
    private Image img;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /*this shows the owner details; the total username, name, email and password.*/
    Owner(int id, String n, String e, String p, Date d) {
        this.id = id;
        this.name = n;
        this.email = e;
        this.Password = p;
        this.dob = d;
    }

    Owner(int id, String n, String e, String p, Date d, Image i) {
        this(id, n, e, p, d);
        this.img = i;
    }

    Owner(int id, String n, String e, String p, Date d, int tw) {
        this(id, n, e, p, d);
        this.tot_walks = tw;
    }

    Owner(int id, String n, String e, String p, Date d, int tw, double dis) {
        this(id, n, e, p, d);
        this.tot_dis = dis;
        this.tot_walks = tw;
    }

    /*Getters and Setters.*/
    public double getTot_dis() {
        return tot_dis;
    }

    public void setTot_dis(double tot_dis) {
        this.tot_dis = tot_dis;
    }

    public int getTot_walks() {
        return tot_walks;
    }

    public void setTot_walks(int tot_walks) {
        this.tot_walks = tot_walks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }
}
