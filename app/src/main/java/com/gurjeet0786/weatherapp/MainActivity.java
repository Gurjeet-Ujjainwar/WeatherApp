package com.gurjeet0786.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.VoiceInteractor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.ConversationActions;
import android.view.textclassifier.TextLinks;
import android.view.textclassifier.TextSelection;
import android.webkit.SafeBrowsingResponse;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mahfa.dnswitch.DayNightSwitch;
import com.mahfa.dnswitch.DayNightSwitchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    ImageView sun, dayland, nightland;
    View daySky, nightSky;
    TextView dayTv, nightTv;
    EditText etCity, etCountry;
    TextView tvResult;
    private final String url = "http://api.openweathermap.org/data/2.5/weather?q=";
    private final String appid = "f797f917725cbeccc1461c2fd49adb19";
    DecimalFormat df = new DecimalFormat("#.##");

    DayNightSwitch dayNightSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCity = findViewById(R.id.etCity);
        etCountry = findViewById(R.id.etCountry);
        tvResult = findViewById(R.id.tvResult);

        sun = findViewById(R.id.sun);
        dayland = findViewById(R.id.day_landscape);
        nightland = findViewById(R.id.night_landscape);
        daySky = findViewById(R.id.day_bg);
        nightSky = findViewById(R.id.night_bg);

        dayNightSwitch = findViewById(R.id.day_night_switch);
        sun.setTranslationY(-110);
        dayNightSwitch.setListener(new DayNightSwitchListener() {
            @Override
            public void onSwitch(boolean is_night) {
                if (is_night) {
                    sun.animate().translationY(110).setDuration(1000);
                    dayland.animate().alpha(0).setDuration(1300);
                    daySky.animate().alpha(0).setDuration(1300);

                } else {
                    sun.animate().translationY(-110).setDuration(1000);
                    dayland.animate().alpha(1).setDuration(1300);
                    daySky.animate().alpha(1).setDuration(1300);
                }

            }
        });
    }
        public void getWeatherDetails (View view){
            String tempUrl = "";
            String city = etCity.getText().toString().trim();
            String country = etCountry.getText().toString().trim();
            if (city.equals("")) {
                tvResult.setText("City field can't be empty!");
            } else {
                if (!country.equals("")) {
                    tempUrl = url + city + "&appid=" + appid;
                } else {
                    tempUrl = url + city + "&appid=" + appid;
                }
                StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("resonse", response);
                        String output = "";
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                            String description = jsonObjectWeather.getString("description");
                            JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                            double temp = jsonObjectMain.getDouble("temp") - 273.15;
                            double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                            float pressure = jsonObjectMain.getInt("pressure");
                            int humidity = jsonObjectMain.getInt("humidity");
                            JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                            String wind = jsonObjectWind.getString("speed");
                            JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                            String clouds = jsonObjectClouds.getString("all");
                            JSONObject jsonObjectSys = jsonResponse.getJSONObject("Sys");
                            String countryName = jsonObjectSys.getString("country");
                            String cityName = jsonResponse.getString("name");
                            tvResult.setTextColor(Color.rgb(68, 134, 199));
                            output = "Current Weather of " + cityName + "(" + countryName + ")"
                                    + "\n temp:" + df.format(temp) + "°C"
                                    + "\n Feels Like: " + df.format(feelsLike) + "°C"
                                    + "\n Humidity: " + humidity + "%"
                                    + "\n Description: " + description
                                    + "\n Wind Speed: " + wind + "meter/sec"
                                    + "\n Cloudiness: " + clouds + "%"
                                    + "\n Pressure: " + pressure + "hPa";
                            tvResult.setText(output);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);

            }

        }

    }

