/*
  * Copyright (C) 2016 Ilnur Saifutdinov (Ильнур Сайфутдинов)
  * e-mail: ilnursoft@gmail.com
  * MIT License
  * https://opensource.org/licenses/mit-license.php
*/


package ru.kaefik.isaifutdinov.an_weather_widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ru.kaefik.isaifutdinov.an_weather_widget.city.CityModel;

/**
 * Implementation of App Widget functionality.
 */
public class AnWeatherWidget extends AppWidgetProvider {

    public CityModel mCityModel;

    private TextView cityNameText;
    private TextView windText;
    private TextView tempCityText;
    private TextView timeRefreshText;
    private ImageView weatherImageView;

    private cityInfoAsyncTask mTask;

    class cityInfoAsyncTask extends AsyncTask<Void, Void, CityModel> {
        @Override
        protected CityModel doInBackground(Void... voids) {
            try {
                // TODO: не нравится что использую в этом классе объект mCityDataWeather
                mCityModel.getHttpWeather();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return mCityModel;
        }

        @Override
        protected void onPostExecute(CityModel cityModel) {
            super.onPostExecute(cityModel);
//            refreshData2View(cityModel);

        }
    }

    // обновление данных о погоде
    public void refreshDataWeather() throws ExecutionException, InterruptedException {
        if (mTask != null) {
            mTask.cancel(true);
        }
        mTask = new cityInfoAsyncTask();
        try {
            mTask.execute();
            mCityModel = mTask.get(3, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
//            Toast.makeText(this, "Ошибка обновления данных", Toast.LENGTH_SHORT);
        }
//        refreshData2View(mCityDataWeather);
    }



    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
//        mCityModel = new CityModel("Kazan");
//        mCityModel.setId("76d6de6e46c704733f12c8738307dbb5");
//        mCityModel.getHttpWeather();

//        CharSequence widgetText =;
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.an_weather_widget);


//        views.setTextViewText(R.id.cityNameText, );


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        mCityModel = new CityModel("Kazan");
        mCityModel.setMYAPPID("76d6de6e46c704733f12c8738307dbb5");
        try {
            mCityModel.getHttpWeather();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public void refreshDataView(CityModel cityModel) {

    }
}

