package com.example.myknowyourgovermentapp;

import java.io.Serializable;

public class Official implements Serializable {

    private String office;
    private String name;
    private String party;
    private String photo_url;
    private String office_address;
    private String phone_number;
    private String email;
    private String Website;
    private String facebook;
    private String twitter;
    private String youtube;

    Official(String office, String name, String party){
        this.office = office;
        this.name = name;
        this.party = party;
    }

    public String getOffice() {
        return office;
    }

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public String getOffice_address() {
        return office_address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return Website;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getYoutube() {
        return youtube;
    }
}
