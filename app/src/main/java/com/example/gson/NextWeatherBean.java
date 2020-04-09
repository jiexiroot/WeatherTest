package com.example.gson;

import android.widget.ImageView;
import android.widget.TextView;

public class NextWeatherBean {
    private int dayImageID;
    private String WeekName;
    private String NextName;

    public int getDayImageID() {
        return dayImageID;
    }

    public String getWeekName() {
        return WeekName;
    }

    public String getNextName() {
        return NextName;
    }

    public String getMaxName() {
        return MaxName;
    }

    public String getMinName() {
        return MinName;
    }

    public NextWeatherBean(int dayImageID, String weekName, String nextName, String maxName, String minName) {
        this.dayImageID = dayImageID;
        WeekName = weekName;
        NextName = nextName;
        MaxName = maxName;
        MinName = minName;
    }

    private String MaxName;
    private String MinName;
}
