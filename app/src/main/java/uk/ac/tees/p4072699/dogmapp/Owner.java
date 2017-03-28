package uk.ac.tees.p4072699.dogmapp;

import android.media.Image;

import java.util.Date;

/**
 * Created by p4061644 on 07/03/2017.
 */

public class Owner {
    private int id;
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

    Owner(int id, String n, String e, String p, Date d){
        this.id = id;
        this.name = n;

        this.email = e;
        this.Password = p;
        this.dob = d;
    }

    Owner(int id,String n, String e, String p, Date d, Image i){
        this(id,n,e,p,d);
        this.img = i;
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
