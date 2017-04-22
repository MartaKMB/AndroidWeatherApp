package com.example.rc.samples;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.weatherIcon)
    protected ImageView weatherIcon;

    @BindView(R.id.outBox)
    protected LinearLayout outBox;

    @BindView(R.id.cityName)
    protected TextView cityName;

    @BindView(R.id.description)
    protected TextView description;

    @BindView(R.id.temperature)
    protected TextView temperature;

    @BindView(R.id.city_name_editText)
    protected EditText city_name_editText;

    private Unbinder bind;
    private HttpURLConnection urlConnection;
    private URL url;

    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?appid=c3fa51e3fdb68faa8b4e48256f5b1dbf&q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bind = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    @OnClick(R.id.submit)
    public void submitForm() {

        if (city_name_editText.getText().length() == 0) {
            city_name_editText.requestFocus();
            city_name_editText.setError("This field can't be empty.");
        }  else {
            loadWeather(city_name_editText.getText().toString());
        }
    }

    private void loadWeather(final String cityName) {

        new AsyncTask<Void, Void, WeatherModel>() {

            @Override
            protected WeatherModel doInBackground(Void... params) {
                StringBuilder sb = new StringBuilder();
                urlConnection = null;

                try {
                    url = new URL(BASE_URL + cityName);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();
                    while (data != -1) {
                        char current = (char) data;
                        data = isw.read();
                        sb.append(current);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
                try {
                    return WeatherModel.serialize(sb.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(WeatherModel model) {

                if (model != null) {
                    bindOutBox(model);
                } else {
                    startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                }
            }
        }.execute();
    }

    private void bindOutBox(WeatherModel model) {

        outBox.setVisibility(View.VISIBLE);

        description.setText(model.getDesccription());
        temperature.setText(String.format("%.2f", model.getTemperature()) + " °C");
        cityName.setText(model.getCityName());

        if (model.getClouds() < 30) {
            weatherIcon.setImageResource(R.drawable.white_balance_sunny);
        } else if (model.getClouds() < 60) {
            weatherIcon.setImageResource(R.drawable.weather_partlycloudy);
        } else {
            weatherIcon.setImageResource(R.drawable.weather_cloudy);
        }

        if (model.getTemperature() > 25) {
            temperature.setTextColor(ContextCompat.getColor(this, R.color.heat));
        } else if (model.getTemperature() > 10) {
            temperature.setTextColor(ContextCompat.getColor(this, R.color.nice));
        } else {
            temperature.setTextColor(ContextCompat.getColor(this, R.color.cold));
        }
    }
}


