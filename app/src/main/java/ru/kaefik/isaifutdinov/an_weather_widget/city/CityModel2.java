package ru.kaefik.isaifutdinov.an_weather_widget.city;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Map;

import ru.kaefik.isaifutdinov.an_weather_widget.AddNewCityActivity;
import ru.kaefik.isaifutdinov.an_weather_widget.utils.Utils;

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

    // копирование объекта obj в текущий
    public void copyCityModel2(CityModel2 obj) {
        this.id = obj.getId();
        this.name = obj.getName();
        this.mainWeather = obj.getMainWeather();
        this.wind = obj.getWind();
        this.sysWeather = obj.getSysWeather();
        this.clouds = obj.getClouds();
        this.weather = obj.getWeather();
        this.mTimeRefresh = obj.getTimeRefresh();
    }

    public CityModel2(String name) {
        this.name = name;
    }


    //----- методы получения данных о погоде
    // получение температуры
    public Float getTemp() {
        if (this.getMainWeather() == null) {
            return 0f;
        } else {
            return this.getMainWeather().get("temp");
        }
    }

    //получение направление ветра
    public Float getWinddirection() {
        if (this.getWind() == null) {
            return 0f;
        } else {
            return this.getWind().get("deg");
        }
    }

    // получение скоркости ветра
    public Float getWindspeed() {

        if (this.getWind() == null) {
            return 0f;
        } else {
            return this.getWind().get("speed");
        }
    }

    public String getWeatherDescription() {
        if (this.getWeather() == null) {
            return "";
        } else {
            return this.getWeather().get(0).get("description");
        }
    }

    // возвращает название файла иконки состояния погоды
    public String getWeatherIcon() {

        if (this.getWeather() == null) {
            return "";
        } else {
            return this.getWeather().get(0).get("icon");
        }
    }

    // возвращает текущую страну
    public String getCountry() {
        if (this.getSysWeather() == null) {
            return "";
        } else {
            return this.getSysWeather().get("country");
        }
    }

    public Boolean isEmptyWeatherDescription() {

        if (this.getWeatherDescription().trim().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    // открыть файл nameFile сохраненный в виде Josn и сохранить данные в объект,
    // возвращает false если произошла ошибка открытия, иначе true
    public boolean openFile(String nameFile, Context context) {
        boolean flagStatus = true;
        Gson gson = new Gson();
        String strjo = Utils.openFile(nameFile, context);
        if (!strjo.trim().equals("")) {
            Log.i(AddNewCityActivity.TAG_SERVICE, "CityModel2 - >  openFile:  strjo  " + strjo);
            CityModel2 cc = gson.fromJson(strjo, CityModel2.class);
            Log.i(AddNewCityActivity.TAG_SERVICE, "CityModel2 - >  gson");
            Log.i(AddNewCityActivity.TAG_SERVICE, "CityModel2 - >  gson.fromJson:  cc.name  " + cc.getName());
            this.copyCityModel2(cc);
            Log.i(AddNewCityActivity.TAG_SERVICE, "CityModel2 - >  gson.fromJson:  this.name  " + this.getName());
        }
        //TODO: реализовать открытие файла и считывание данных в формате JOSN

        return flagStatus;
    }

    // сохранить объект в файл nameFile в виде Josn
    public void saveToFile(String nameFile, Context context) throws JSONException {
        // TODO: реализовать сохранение в файл формате JOSN
        Gson gson = new Gson();
        String strJo = gson.toJson(this).toString();
        Log.i(AddNewCityActivity.TAG_SERVICE,strJo);
//        String strJo = this.toJSON().toString();
        Utils.saveFile(nameFile, strJo, context);    }


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
