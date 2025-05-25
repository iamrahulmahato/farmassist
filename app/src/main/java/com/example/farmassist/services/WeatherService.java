package com.example.farmassist.services;

import com.example.farmassist.models.WeatherData;
import org.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherService {
    private static final String API_KEY = "d99f98eae56fc4dffb250716beb515a2";
    private static final String CITY_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";
    private static final String LOCATION_URL = "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s&units=metric";

    public WeatherData getWeatherByCity(String city) throws Exception {
        String url = String.format(CITY_URL, city, API_KEY);
        return fetchWeather(url);
    }

    public WeatherData getWeatherByLocation(double lat, double lon) throws Exception {
        String url = String.format(LOCATION_URL, lat, lon, API_KEY);
        return fetchWeather(url);
    }

    private WeatherData fetchWeather(String url) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            String json = response.body().string();
            JSONObject jsonObject = new JSONObject(json);

            WeatherData data = new WeatherData();
            data.setLocation(jsonObject.getString("name"));
            data.setTemperature(jsonObject.getJSONObject("main").getDouble("temp"));
            data.setConditions(jsonObject.getJSONArray("weather")
                    .getJSONObject(0).getString("description"));
            return data;
        } else {
            throw new Exception("Failed to fetch weather");
        }
    }
}
