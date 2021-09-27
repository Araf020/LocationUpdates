package com.google.android.gms.location.sample.locationupdates;

public class Person {
    String ID;
    String Zone;
    String lat,lang;

    public Person(){

    }
    public Person(String ID, String zone, String lat, String lang) {
        this.ID = ID;
        Zone = zone;
        this.lat = lat;
        this.lang = lang;
    }

    public String getZone() {
        return Zone;
    }

    public void setZone(String zone) {
        Zone = zone;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
