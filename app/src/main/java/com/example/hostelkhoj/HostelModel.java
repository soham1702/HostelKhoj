package com.example.hostelkhoj;

public class HostelModel {
    String uid,hostelname,ownername,type,Availablecots,contact,description,email;
    double loclattitude,loclongitude;

    public HostelModel() {

    }

    public HostelModel(String uid, String hostelname, String ownername, String type, String availablecots, String contact, String description, String email, double loclattitude, double loclongitude) {
        this.uid = uid;
        this.hostelname = hostelname;
        this.ownername = ownername;
        this.type = type;
        Availablecots = availablecots;
        this.contact = contact;
        this.description = description;
        this.email = email;
        this.loclattitude = loclattitude;
        this.loclongitude = loclongitude;
    }

    public HostelModel(String uid, String hostelname, String ownername, String type) {
        this.uid = uid;
        this.hostelname = hostelname;
        this.ownername = ownername;
        this.type = type;
    }

    public String getAvailablecots() {
        return Availablecots;
    }

    public void setAvailablecots(String availablecots) {
        Availablecots = availablecots;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLoclattitude() {
        return loclattitude;
    }

    public void setLoclattitude(double loclattitude) {
        this.loclattitude = loclattitude;
    }

    public double getLoclongitude() {
        return loclongitude;
    }

    public void setLoclongitude(double loclongitude) {
        this.loclongitude = loclongitude;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHostelname() {
        return hostelname;
    }

    public void setHostelname(String hostelname) {
        this.hostelname = hostelname;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
