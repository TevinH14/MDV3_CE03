package com.example.hamiltontevin_ce03;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.hamiltontevin_ce03.fragment.ForecastFragment;


public class ForecastActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        ForecastFragment forecastFragment = ForecastFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_forecastContainer,forecastFragment,ForecastFragment.TAG)
                .commit();
    }


}
