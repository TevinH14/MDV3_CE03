package com.example.hamiltontevin_ce03.file;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.hamiltontevin_ce03.modal.WeatherLocationData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileStorageHelper {
    private static final String CURRENT_WEATHER_FILE_NAME = "CURRENT_WEATHER";
    private static final String WEEKLY_WEATHER_FILE_NAME = "WEEKLY_WEATHER";
    private static final String FILE_TYPE = ".ser";
    private static final String ICON_FILE_NAME= "WEATHER_ICON.png";
    private static  File  IMAGE_DIRECTORY = null;

    private final Context mContext;
    public FileStorageHelper(Context mContext) {
        this.mContext = mContext;
    }

    public  void saveCurrentWeatherData(WeatherLocationData weatherData){
        if(mContext != null & weatherData != null) {
            try{
                FileOutputStream fos =
                        mContext
                        .openFileOutput(
                                CURRENT_WEATHER_FILE_NAME+FILE_TYPE,
                                Context.MODE_PRIVATE);

                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(weatherData);
                fos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            Log.i("test", "data saved");
        }
    }

    public  void saveWeaklyWeatherData(ArrayList<WeatherLocationData> weatherDataList){
        if(mContext != null & weatherDataList != null) {
            try{
                FileOutputStream fos =
                        mContext
                                .openFileOutput(
                                        WEEKLY_WEATHER_FILE_NAME+FILE_TYPE,
                                        Context.MODE_PRIVATE);

                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(weatherDataList);
                fos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public  WeatherLocationData getCurrentWeatherData(){
        WeatherLocationData wld = null;
        File file = mContext.getFileStreamPath(CURRENT_WEATHER_FILE_NAME+FILE_TYPE);
        ObjectInputStream ois;
        if(file.exists()){
            try{
                FileInputStream fis = mContext
                        .openFileInput(CURRENT_WEATHER_FILE_NAME+FILE_TYPE);

                ois = new ObjectInputStream(fis);
                Object object = ois.readObject();

                 wld = (WeatherLocationData)object;
                 ois.close();
            }catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }
        else {
            return null;
        }
        return wld;
    }

    public  ArrayList getWeeklyWeatherData(){
        ArrayList wldl = new ArrayList<>();
        File file = mContext
                .getFileStreamPath(WEEKLY_WEATHER_FILE_NAME+FILE_TYPE);

        if(file.exists()){
            try{
                FileInputStream fis = mContext
                        .openFileInput(WEEKLY_WEATHER_FILE_NAME+FILE_TYPE);

                ObjectInputStream ois = new ObjectInputStream(fis);
                Object object = ois.readObject();

                wldl = ((ArrayList)object);
                ois.close();
            }catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }
        else {
            return null;
        }
        return wldl;
    }

    public void saveBitmap(Bitmap icon){
        ContextWrapper cw = new ContextWrapper(mContext.getApplicationContext());

        IMAGE_DIRECTORY = cw.getDir(ICON_FILE_NAME, Context.MODE_PRIVATE);
        // Create imageDir
        File path = new File(IMAGE_DIRECTORY,"icon.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            icon.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if( fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap getBitmap() {
        Bitmap icon = null;
        FileInputStream fis;
        Log.i("test","loading icon");
        try {
            File iconFile = new File(IMAGE_DIRECTORY, "icon.jpg");
             fis = new FileInputStream(iconFile);
            icon = BitmapFactory.decodeStream(fis);
            fis.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Log.i("test","loading icon successful");

        return icon ;
    }
}
