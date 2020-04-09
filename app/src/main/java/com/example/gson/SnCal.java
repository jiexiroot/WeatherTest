package com.example.gson;


import org.litepal.crud.LitePalSupport;

public class SnCal extends LitePalSupport {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    private String cityname;
}
