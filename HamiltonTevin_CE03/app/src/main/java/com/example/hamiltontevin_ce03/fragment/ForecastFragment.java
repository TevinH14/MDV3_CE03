package com.example.hamiltontevin_ce03.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.hamiltontevin_ce03.R;
import com.example.hamiltontevin_ce03.adpater.adapter.WeatherAdapter;
import com.example.hamiltontevin_ce03.file.FileStorageHelper;

import java.util.ArrayList;

public class ForecastFragment extends ListFragment {
    public static final String TAG = "ForecastFragment.TAG";

    public static ForecastFragment newInstance() {

        Bundle args = new Bundle();

        ForecastFragment fragment = new ForecastFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weekly_weather_list,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList wldArray;

        if(getContext() != null){
            FileStorageHelper fsh = new FileStorageHelper(getContext());
            wldArray = fsh.getWeeklyWeatherData();
            if (wldArray != null && wldArray.size() > 0) {
                WeatherAdapter wa = new WeatherAdapter(getContext(), wldArray);
                setListAdapter(wa);
            }
        }
    }

}
