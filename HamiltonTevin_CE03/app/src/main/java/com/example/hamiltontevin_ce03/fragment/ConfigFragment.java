package com.example.hamiltontevin_ce03.fragment;

import android.os.Bundle;


import androidx.preference.PreferenceFragmentCompat;

import com.example.hamiltontevin_ce03.R;

public class ConfigFragment extends PreferenceFragmentCompat {
    public static final String TAG = "ConfigFragment.TAG";

    public static ConfigFragment newInstance() {
        return new ConfigFragment();
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.prefs_config,rootKey);
    }


}
