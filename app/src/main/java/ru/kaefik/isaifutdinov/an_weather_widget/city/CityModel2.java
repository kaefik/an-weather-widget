package ru.kaefik.isaifutdinov.an_weather_widget.city;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;

// класс информации о погоде города - вторая редакция специально для разбора json c помощью gson
public class CityModel2 {
    private long id; // ID города
    private String name;  // название города
    @SerializedName("main")
    private Map<String, Float> mainWeather; //  "main":{"temp":283.15,"humidity":81,"pressure":1015,"temp_min":283.15,"temp_max":283.15}
    private Map<String, Float> wind;  // "wind":{"speed":1.03,"gust":0,"deg":0}
    @SerializedName("sys")
    private Map<String, String> sysWeather; //  "sys":{"country":"RU"}
    private Map<String, Long> clouds; // "clouds":{"all":12}
    private ArrayList<Map<String, String>> weather; // weather":[{"id":801,"main":"Clouds","description":"few clouds","icon":"02d"}]

    private String mTimeRefresh; // время обновления прогноза погоды


    public CityModel2(Map<String, Long> clouds, long id, Map<String, Float> mainWeather, String name, Map<String, String> sysWeather, ArrayList<Map<String, String>> weather, Map<String, Float> wind) {
        this.clouds = clouds;
        this.id = id;
        this.mainWeather = mainWeather;
        this.name = name;
        this.sysWeather = sysWeather;
        this.weather = weather;
        this.wind = wind;
    }


    //----- методы получения данных о погоде
    // получение температуры
    public Float getTemp() {
        return this.getMainWeather().get("temp");
    }

    //получение направление ветра
    public Float getWinddirection() {
        return this.getWind().get("deg");
    }

    // получение скоркости ветра
    public Float getWindspeed() {
        return this.getWind().get("speed");
    }

    public String getWeatherDescription(){
        return  this.getWeather().get(0).get("description");
    }

    // возвращает название файла иконки состояния погоды
    public String getWeatherIcon(){
        return this.getWeather().get(0).get("icon");
    }

    //----- END методы получения данных о погоде


    //---------- основные методы

    public String getTimeRefresh() {
        return mTimeRefresh;
    }

    public void setTimeRefresh(String timeRefresh) {
        mTimeRefresh = timeRefresh;
    }

    public ArrayList<Map<String, String>> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<Map<String, String>> weather) {
        this.weather = weather;
    }

    public CityModel2(String name) {
        this.name = name;
    }

    public CityModel2() {
        this.name = "";
        id = 0;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<String, Float> getMainWeather() {
        return mainWeather;
    }

    public void setMainWeather(Map<String, Float> mainWeather) {
        this.mainWeather = mainWeather;
    }

    public Map<String, Long> getClouds() {
        return clouds;
    }

    public void setClouds(Map<String, Long> clouds) {
        this.clouds = clouds;
    }

    public Map<String, String> getSysWeather() {
        return sysWeather;
    }

    public void setSysWeather(Map<String, String> sysWeather) {
        this.sysWeather = sysWeather;
    }

    public Map<String, Float> getWind() {
        return wind;
    }

    public void setWind(Map<String, Float> wind) {
        this.wind = wind;
    }
    //---------- основные методы


}
