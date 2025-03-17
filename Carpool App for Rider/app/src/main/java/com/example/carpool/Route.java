package com.example.carpool;

import com.google.firebase.database.PropertyName;

public class Route {
    String From,To,Time,Price,Driver_Phone,Driver_Name,Ride_ID,Status, Driver_Mail;
     String Date;


    public String getFrom() {
        return From;
    }

    public String getTo() {
        return To;
    }

    public String getTime() {
        return Time;
    }

    public String getPrice() {
        return Price;
    }


    public String getDate() {
        return Date;
    }

    public String getDriver_Phone() {
        return Driver_Phone;
    }

    public String getDriver_Name() {
        return Driver_Name;
    }

    public String getRide_ID() {
        return Ride_ID;
    }

    public String getStatus() {
        return Status;
    }

    public String getDriver_Mail() {
        return Driver_Mail;
    }
}

