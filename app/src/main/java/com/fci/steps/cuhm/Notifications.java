package com.fci.steps.cuhm;

/**
 * Created by Mohamed on 6/6/2018.
 */

public class Notifications {
    String first_name,last_name,title ,message ;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public  Notifications (String first_name , String last_name , String title , String message)
    {
        this.first_name=first_name;
        this.last_name=last_name;

        this.title =title;
        this.message =message ;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
