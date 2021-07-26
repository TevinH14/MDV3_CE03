package com.example.hamiltontevin_ce03.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.hamiltontevin_ce03.file.FileStorageHelper;
import com.example.hamiltontevin_ce03.modal.WeatherLocationData;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils extends AsyncTask<String, Bitmap, Bitmap> {

	//get json string of weather data
	private static String getNetworkData(String _url) {

		if(_url == null){
			return null;
		}

		HttpsURLConnection connection = null;
		String data = null;
		URL url;

		try {
			url = new URL(_url);

			connection = (HttpsURLConnection)url.openConnection();
			connection.connect();

		} catch (Exception e) {
			e.printStackTrace();
		}
		InputStream is = null;
		try{
			if(connection != null){
				is = connection.getInputStream();
				data = IOUtils.toString(is, "UTF_8");
			}

		}catch (Exception e){
			e.printStackTrace();
		}
		finally {
			if(connection != null){
				if(is != null){
					try{
						is.close();
					}catch(Exception e){
						e.printStackTrace();
					}
					connection.disconnect();
				}
			}
		}

		return data;
	}


	public static WeatherLocationData getWeatherJsonData(String _url, Context context) {
		if(_url == null){
			return null;
		}
		String data = null;
		try {
			data = NetworkUtils.getNetworkData(_url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(data != null) {
			try {
				String condition = "";
				String icon = "";
				String currentTemp ;
				String format ;

				JSONObject response = new JSONObject(data);
				JSONObject jsonObject= response.getJSONObject("current");
				currentTemp = jsonObject.getString("temp");
				JSONArray weatherJSONArray = jsonObject.getJSONArray("weather");

				for (int i = 0; i < weatherJSONArray.length(); i++) {
					JSONObject obj = weatherJSONArray.getJSONObject(i);

					condition = obj.getString("main");
					 icon = obj.getString("icon");
				}
				FileStorageHelper fsh = new FileStorageHelper(context);
				Bitmap iconBitmap = getWeatherIcon(icon);
				if(iconBitmap != null) {
					fsh.saveBitmap(iconBitmap);
				}

				@SuppressLint("SimpleDateFormat")
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
				 format = simpleDateFormat.format(new Date());

				WeatherLocationData weatherLocationData = null;
				if (!condition.equals("") && !format.equals("")
						&& !icon.equals("") && !currentTemp.equals("")) {

					double currentTempDouble = Double.parseDouble(currentTemp);
					int rounderDouble = (int) Math.round(currentTempDouble);
					currentTemp = String.valueOf(rounderDouble);
					 weatherLocationData = new WeatherLocationData(condition
							, currentTemp
							, format
							, icon);
				}
				SaveWeeklyWeather(response,context);

				return weatherLocationData;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	//get weekly data
	private static void SaveWeeklyWeather(JSONObject baseObject,Context context){
		ArrayList<WeatherLocationData> weeklyArrayList = new ArrayList<>();
		try {
			JSONArray jsonArray = baseObject.getJSONArray("daily");
			for (int i = 0; i < jsonArray.length(); i++) {
				String icon = null;
				JSONObject obj = jsonArray.getJSONObject(i);
				JSONObject tempObject = obj.getJSONObject("temp");

				String lowest = tempObject.getString("min");
				String highest = tempObject.getString("max");
				double lowestTemp = Double.parseDouble(lowest);
				int roundedLowestDouble = (int) Math.round(lowestTemp);
				lowest = String.valueOf(roundedLowestDouble);

				double highestTemp = Double.parseDouble(highest);
				int roundedHighestDouble = (int) Math.round(highestTemp);
				highest = String.valueOf(roundedHighestDouble);

				JSONArray weatherArray = obj.getJSONArray("weather");

				for (int w = 0; w < weatherArray.length(); w++) {
					JSONObject weatherObj = weatherArray.getJSONObject(w);
					 icon = weatherObj.getString("icon");
				}
				weeklyArrayList.add(new WeatherLocationData(icon,highest,lowest));
			}
			FileStorageHelper fsh = new FileStorageHelper(context);
			fsh.saveWeaklyWeatherData(weeklyArrayList);
		}
		catch (JSONException e){
			e.printStackTrace();
		}
	}


	//get icon from url and return bit map to be passed into image view.
		@Override
	protected  Bitmap doInBackground(String... strings) {
		if(strings == null || strings.length <= 0 || strings[0].trim().isEmpty()){
			return null;
		}
		Bitmap weatherIcon;
		try {
			String iconUrl = "http://openweathermap.org/img/w/" + strings[0] + ".png";
			URL url = new URL(iconUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			InputStream is = connection.getInputStream();
			weatherIcon = BitmapFactory.decodeStream(is);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return weatherIcon;
	}

	private static Bitmap getWeatherIcon(String iconString){
		Bitmap weatherIcon;
		try {
			String iconUrl = "http://openweathermap.org/img/w/" + iconString + ".png";
			URL url = new URL(iconUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();connection.connect();
			InputStream is = connection.getInputStream();
			weatherIcon = BitmapFactory.decodeStream(is);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return weatherIcon;
	}
}