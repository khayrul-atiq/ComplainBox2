package com.example.olife.complainbox2;

import java.io.Serializable;

/**
 * Created by olife on 4/6/2018.
 */
@SuppressWarnings("serial")
public class EventInformation implements Serializable{

    private String event_title,event_description,event_date,event_publish_date;


    public EventInformation(String event_title, String event_description, String event_date, String event_publish_date) {
        this.event_title = event_title;
        this.event_description = event_description;
        this.event_date = event_date;
        this.event_publish_date = event_publish_date;
    }

    public String getEvent_title() {
        return event_title;
    }

    public String getEvent_description() {
        return event_description;
    }

    public String getEvent_date() {
        return event_date;
    }

    public String getEvent_publish_date() {
        return event_publish_date;
    }
}
