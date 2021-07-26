package com.example.hamiltontevin_ce03.widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import androidx.preference.PreferenceManager;

import com.example.hamiltontevin_ce03.ConfigActivity;
import com.example.hamiltontevin_ce03.ForecastActivity;
import com.example.hamiltontevin_ce03.R;
import com.example.hamiltontevin_ce03.file.FileStorageHelper;
import com.example.hamiltontevin_ce03.modal.WeatherLocationData;
import com.example.hamiltontevin_ce03.services.WeatherIntentService;

import java.text.SimpleDateFormat;
import java.util.Date;


public class WidgetUtil  {
    private static RemoteViews mWidgetViews;
    public static final String EXTRA_GET_WEATHER_DATA = "EXTRA_GET_WEATHER_DATA";

    public static final String EXTRA_WIDGET_ID = "EXTRA_WIDGET_ID";

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetId){

        String theme = getThemePreferenceData(context);
        if(theme != null) {
            if(theme.equals("0")) {
                mWidgetViews = new RemoteViews(context.getPackageName(), R.layout.weather_widget_light);
            }
            else {
                mWidgetViews = new RemoteViews(context.getPackageName(), R.layout.weather_widget_dark);
            }
        }

        Intent confIntent = new Intent(context, ConfigActivity.class);
        confIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,widgetId);
        PendingIntent configPendingIntent = PendingIntent.getActivity(context,widgetId,
                confIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mWidgetViews.setOnClickPendingIntent(R.id.ib_config,configPendingIntent);

        Intent forecastIntent = new Intent(context, ForecastActivity.class);
        PendingIntent forecastPendingIntent = PendingIntent.getActivity(context,widgetId,
                forecastIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mWidgetViews.setOnClickPendingIntent(R.id.ib_icon,forecastPendingIntent);


        if(context != null) {
            FileStorageHelper fsh = new FileStorageHelper(context);
            WeatherLocationData wls = fsh.getCurrentWeatherData();
            if (wls != null) {
                if(!onWeatherUpdate(wls.getTimeStamp())) {
                    mWidgetViews.setTextViewText(R.id.tv_condition, wls.getCondition());
                    mWidgetViews.setTextViewText(R.id.tv_temp, wls.getTemperature() + " F");
                    mWidgetViews.setTextViewText(R.id.tv_timeStamp, wls.getTimeStamp());
                    mWidgetViews.setImageViewBitmap(R.id.ib_icon, fsh.getBitmap());
                }else {
                    getPreferenceLocationData(context,widgetId);
                }
            }
        }
        appWidgetManager.updateAppWidget(widgetId,mWidgetViews);
    }
    static void updateWidget(Context context,AppWidgetManager appWidgetManager,
                             int[] appWidgetIds){
        for(int appWidgetId: appWidgetIds){
            updateWidget(context, appWidgetManager,appWidgetId);
        }
    }
    private static String getThemePreferenceData(Context context){
        String currentTheme;
        SharedPreferences themeChoice = PreferenceManager.getDefaultSharedPreferences(context);
         currentTheme = themeChoice
                .getString(context.getString(R.string.key_list_preference_theme),
                        "0");
         return currentTheme;

    }

    public static void getPreferenceLocationData(Context context, int widgetId){
        SharedPreferences locationChoice = PreferenceManager.getDefaultSharedPreferences(context);
        String currentLocation = locationChoice
                .getString(context.getString(R.string.key_list_preference_location),
                        "can't load");
        // split string to get lat and long of location
        String[] latAndLongString = currentLocation.split(",");

        Intent weatherIntent = new Intent(context, WeatherIntentService.class);
        weatherIntent.putExtra(EXTRA_GET_WEATHER_DATA,latAndLongString);
        weatherIntent.putExtra(WidgetUtil.EXTRA_WIDGET_ID,widgetId);
        context.startService(weatherIntent);
    }

    private static boolean onWeatherUpdate(String timeStamp){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        String format = simpleDateFormat.format(new Date());

        return format.equals(timeStamp);
    }
}
