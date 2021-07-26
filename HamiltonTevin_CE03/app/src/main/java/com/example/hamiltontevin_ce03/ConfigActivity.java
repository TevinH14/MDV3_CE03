package com.example.hamiltontevin_ce03;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hamiltontevin_ce03.fragment.ConfigFragment;
import com.example.hamiltontevin_ce03.widget.WidgetUtil;

public class ConfigActivity extends AppCompatActivity {
    private int mWidgetId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_layout);

        //get the starting intent.
        Intent starter = getIntent();

        // Get the ID of the widget from extras.
        mWidgetId = starter.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);



        if(savedInstanceState == null){
            ConfigFragment fragment = ConfigFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,fragment,ConfigFragment.TAG)
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.config_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_save){


            //close the activity.
            WidgetUtil.getPreferenceLocationData(this,mWidgetId);
            Log.i("test","made back to config activity");

            // Get a widget manager using the activity as a context.
            //AppWidgetManager mgr = AppWidgetManager.getInstance(this);

            finish();
        }
        return true;
    }
}
