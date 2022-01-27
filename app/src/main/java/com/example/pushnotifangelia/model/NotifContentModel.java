package com.example.pushnotifangelia.model;

/**
 * Created by Angelia Widjaja on 21-Jan-22 19:34.
 */
public class NotifContentModel {
    private String title;
    private String message;

    public NotifContentModel(String title, String message) {
        this.title = title;
        this.message = message;
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
