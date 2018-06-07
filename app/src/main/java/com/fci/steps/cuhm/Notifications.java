package com.fci.steps.cuhm;

/**
 * Created by Mohamed on 6/6/2018.
 */

public class Notifications {
    String title ,message ;
    public  Notifications (String title ,String message)
    {
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
