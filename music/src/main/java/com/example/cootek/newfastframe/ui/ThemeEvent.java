package com.example.cootek.newfastframe.ui;

/**
 * Created by COOTEK on 2017/9/6.
 */

public class ThemeEvent {
    private boolean isNight;

    public ThemeEvent(boolean isNight) {
        this.isNight = isNight;
    }


    public boolean isNight() {
        return isNight;
    }

    public void setNight(boolean night) {
        isNight = night;
    }
}
