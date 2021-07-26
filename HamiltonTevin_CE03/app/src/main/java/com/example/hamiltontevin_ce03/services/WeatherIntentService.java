package com.example.hamiltontevin_ce03.services;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.example.hamiltontevin_ce03.file.FileStorageHelper;
import com.example.hamiltontevin_ce03.modal.WeatherLocationData;
import com.example.hamiltontevin_ce03.network.NetworkUtils;
import com.example.hamiltontevin_ce03.widget.WidgetUtil;

public class WeatherIntentService extends IntentService {

    public WeatherIntentService() {
        super(" WeatherIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null && intent.hasExtra(WidgetUtil.EXTRA_GET_WEATHER_DATA)
                && intent.hasExtra(WidgetUtil.EXTRA_WIDGET_ID)){
            int widgetId = intent.getIntExtra(WidgetUtil.EXTRA_WIDGET_ID,0);
           boolean dataSaved = getWeatherData(intent.getStringArrayExtra(
                   WidgetUtil.EXTRA_GET_WEATHER_DATA));
           if(dataSaved && widgetId != 0) {
               AppWidgetManager mgr = AppWidgetManager.getInstance(this);
               WidgetUtil.updateWidget(this, mgr, widgetId);
           }
        }
    }

    private boolean getWeatherData(String[] locationArray){
        String apiStartPoint = "https://api.openweathermap.org/data/2.5/onecall?";
        String apiEndPoint = "&units=imperial&appid=3239aaa19af7dbe7a473f0910c1f529a";
        if(locationArray != null){

            String apiMiddlePoint = "lat=" + locationArray[0] + "&lon=" + locationArray[1];
            String urlString = apiStartPoint + apiMiddlePoint + apiEndPoint;
            WeatherLocationData wld = NetworkUtils.getWeatherJsonData(urlString,this);
            FileStorageHelper fsh = new FileStorageHelper(this);
            fsh.saveCurrentWeatherData(wld);

            return true;
        }
        return false;
    }
}
