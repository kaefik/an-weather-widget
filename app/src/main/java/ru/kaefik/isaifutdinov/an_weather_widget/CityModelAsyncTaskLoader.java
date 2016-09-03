package ru.kaefik.isaifutdinov.an_weather_widget;

import android.content.AsyncTaskLoader;
import android.content.Context;

import ru.kaefik.isaifutdinov.an_weather_widget.city.CityModel;

public class CityModelAsyncTaskLoader extends AsyncTaskLoader<CityModel> {
    public CityModelAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    public CityModel loadInBackground() {
        return null;
    }
}
