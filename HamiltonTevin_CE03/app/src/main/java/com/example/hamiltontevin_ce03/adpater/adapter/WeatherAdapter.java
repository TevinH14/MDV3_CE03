//Tevin Hamilton
//term:2005
//CE03
//
package com.example.hamiltontevin_ce03.adpater.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hamiltontevin_ce03.R;
import com.example.hamiltontevin_ce03.modal.WeatherLocationData;
import com.example.hamiltontevin_ce03.network.NetworkUtils;

import java.util.ArrayList;
import java.util.Calendar;


public class WeatherAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<WeatherLocationData> mWeeklyWeather;

    public WeatherAdapter(Context mContext ,ArrayList<WeatherLocationData> mWeeklyWeather) {
        this.mContext = mContext;
        this.mWeeklyWeather = mWeeklyWeather;

    }

    @Override
    public int getCount() {
        return mWeeklyWeather.size();
    }

    @Override
    public Object getItem(int pos) {
        return mWeeklyWeather.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        WeatherLocationData dayData = (WeatherLocationData) getItem(position);
        Log.i("position","position: " + position);
        if(convertView== null){
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.weekly_weather_layout,parent,false);

            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }
        if(dayData != null){

            getIcon(dayData.getWeatherIcon(),vh);
            vh.day.setText(getDay(position));
            vh.high.setText(dayData.getmHigh());
            vh.low.setText(dayData.getmLow());


        }
        return convertView;
    }

    private static void getIcon(String iconString, final ViewHolder vh){
        NetworkUtils nu = new NetworkUtils(){
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
               vh.icon.setImageBitmap(bitmap);
            }
        };
        nu.execute(iconString);
    }



    static class ViewHolder{
        final TextView day;
        final TextView high;
        final TextView low;
        final ImageView icon;

        ViewHolder(View layout) {
            this.day = layout.findViewById(R.id.tv_day);
            this.high = layout.findViewById(R.id.tv_high);
            this.low = layout.findViewById(R.id.tv_low);
            this.icon = layout.findViewById(R.id.iv_dayIcon);
        }
    }

    private String getDay(int pos){


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,pos);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SUNDAY:
                // Current day is Sunday
                return "SUN";
            case Calendar.MONDAY:
                // Current day is Monday
                return "MON";
            case Calendar.TUESDAY:
                // etc.
                return "TUE";
            case Calendar.WEDNESDAY:
                // Current day is Sunday
                return "WED";
            case Calendar.THURSDAY:
                // Current day is Monday
                return "THUR";
            case Calendar.FRIDAY:
                // etc.
                return "FRI";
            case Calendar.SATURDAY:
                // Current day is Sunday
                return "SAT";
        }
        return null;
    }


}
