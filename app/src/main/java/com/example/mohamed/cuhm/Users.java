package com.example.mohamed.cuhm;

/**
 * Created by Mohamed on 3/17/2018.
 */

public class Users {
    private String fname;
    private String lname;
    private String Thumb_image;

    public String getlname() {
        return lname;
    }

    public void setlname(String lName) {
        this.lname = lName;
    }

    private String job;

    public Users() {}

    public Users(String fname,String lname , String Thumb_image, String job) {
        this.fname = fname;
        this.lname = lname ;
        this.Thumb_image = Thumb_image;
        this.job = job;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getThumb_image() {
        return Thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.Thumb_image = thumb_image;
    }
}
