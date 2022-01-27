package com.example.pushnotifangelia.model;

/**
 * Created by Angelia Widjaja on 21-Jan-22 19:34.
 */
public class PushNotifModel {
    private NotifContentModel data;
    private String to;

    public PushNotifModel(NotifContentModel data, String to) {
        this.data = data;
        this.to = to;
    }

    public NotifContentModel getData() {
        return data;
    }

    public void setData(NotifContentModel data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
