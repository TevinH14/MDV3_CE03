package com.example.hamiltontevin_ce03.modal;

import java.io.Serializable;

public class WeatherLocationData implements Serializable {

    private String mCondition;
    private String mTemperature;
    private String mTimeStamp;
    private final String mWeatherIcon;

    private String mHigh;
    private String mLow;


    public WeatherLocationData(String mCondition, String mTemperature, String mTimeStamp, String mWeatherIcon) {
        this.mCondition = mCondition;
        this.mTemperature = mTemperature;
        this.mTimeStamp = mTimeStamp;
        this.mWeatherIcon = mWeatherIcon;
    }

    public WeatherLocationData(String mWeatherIcon, String mHigh, String mLow) {
        this.mWeatherIcon = mWeatherIcon;
        this.mHigh = mHigh;
        this.mLow = mLow;
    }

    public String getWeatherIcon() {
        return mWeatherIcon;
    }

    public String getCondition() {
        return mCondition;
    }

    public String getTemperature() {
        return mTemperature;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public String getmHigh() {
        return mHigh;
    }

    public String getmLow() {
        return mLow;
    }
}
