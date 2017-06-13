package com.jordan.lucie.utrackapp;

import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * UTrackObject class
 * @author Group 1
 * @version 1.0
 */

public class UTrackObject {

    private String name;
    private double latitude;
    private double longitude;
    private String notes;

    public UTrackObject(String name, double latitude, double longitude, String notes) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.notes = notes;
    }

    public UTrackObject(){
        this.name = "null";
        this.latitude = 0;
        this.longitude = 0;
        this.notes = "null";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String toString(){
        return name +", (" + latitude + ", " + longitude + "), " + notes;
    }

}

