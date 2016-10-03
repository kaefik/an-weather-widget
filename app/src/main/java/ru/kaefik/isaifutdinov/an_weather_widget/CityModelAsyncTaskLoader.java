package ru.kaefik.isaifutdinov.an_weather_widget;

import android.content.AsyncTaskLoader;
import android.content.Context;

import ru.kaefik.isaifutdinov.an_weather_widget.city.CityModel2;

public class CityModelAsyncTaskLoader extends AsyncTaskLoader<CityModel2> {
    public CityModelAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    public CityModel2 loadInBackground() {
        return null;
    }
}
