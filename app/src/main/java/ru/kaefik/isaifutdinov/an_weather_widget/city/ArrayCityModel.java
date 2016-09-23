package ru.kaefik.isaifutdinov.an_weather_widget.city;

// массив данных
public class ArrayCityModel {
    long count; // кол-во найденных городов
    CityModel2[] list;

    public ArrayCityModel(long count, CityModel2[] list) {
        this.count = count;
        this.list = list;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void setList(CityModel2[] list) {
        this.list = list;
    }

    public long getCount() {
        return count;
    }

    public CityModel2[] getList() {
        return list;
    }
    public CityModel2 getList(int ii) {
        return list[ii];
    }
}
