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
}
