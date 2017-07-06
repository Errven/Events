package com.erven.events;

import java.util.ArrayList;

/**
 * Created by kryst on 05/07/2017.
 */

public class Event {
    public String id;
    public String name;
    public String place;
    public String date;
    public String time;
    public String owner;
    public ArrayList<String> users;

    public Event(String id, String name, String place, String date, String time, String owner, ArrayList<String> users) {
        this.id = id;
        this.name = name;
        this.place = place;
        this.date = date;
        this.time = time;
        this.owner = owner;
        this.users = users;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlace() {
        return place;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getOwner() {
        return owner;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public Event() {

    }
}
