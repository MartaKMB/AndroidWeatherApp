package com.example.rc.samples;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherModel {

    private String cityName;
    private String desccription;
    private Double temperature;
    private Integer clouds;

    public WeatherModel(String cityName, String desccription, Double temperature, Integer clouds) {
        this.cityName = cityName;
        this.desccription = desccription;
        this.temperature = temperature;
        this.clouds = clouds;
    }

    public static WeatherModel serialize(String json) throws JSONException {
        JSONObject root = new JSONObject(json);

        String name = root.getString("name");
        Double tempTemperature = root.getJSONObject("main").getDouble("temp");
        Integer clouds = root.getJSONObject("clouds").getInt("all");
        JSONArray weatherArray = root.getJSONArray("weather");

        Double temperature = tempTemperature - 273.15;

        String desc = "";
        if (weatherArray.length() > 0) {
            desc = ((JSONObject) weatherArray.get(0)).getString("description");
        }
        return new WeatherModel(name, desc, temperature, clouds);
    }

    public String getCityName() {
        return cityName;
    }

    public String getDesccription() {
        return desccription;
    }

    public Double getTemperature() {
        return temperature;
    }

    public Integer getClouds() {
        return clouds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeatherModel that = (WeatherModel) o;

        if (cityName != null ? !cityName.equals(that.cityName) : that.cityName != null)
            return false;
        if (desccription != null ? !desccription.equals(that.desccription) : that.desccription != null)
            return false;
        if (temperature != null ? !temperature.equals(that.temperature) : that.temperature != null)
            return false;
        return clouds != null ? clouds.equals(that.clouds) : that.clouds == null;

    }

    @Override
    public int hashCode() {
        int result = cityName != null ? cityName.hashCode() : 0;
        result = 31 * result + (desccription != null ? desccription.hashCode() : 0);
        result = 31 * result + (temperature != null ? temperature.hashCode() : 0);
        result = 31 * result + (clouds != null ? clouds.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "cityName= " + cityName +
                ", \n desccription= " + desccription +
                ", \n temperature= " + temperature +
                ", \n clouds= " + clouds;
    }
}
