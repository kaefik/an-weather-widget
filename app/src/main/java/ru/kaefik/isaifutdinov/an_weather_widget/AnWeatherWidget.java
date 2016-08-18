/*
  * Copyright (C) 2016 Ilnur Saifutdinov (Ильнур Сайфутдинов)
  * e-mail: ilnursoft@gmail.com
  * MIT License
  * https://opensource.org/licenses/mit-license.php
*/


package ru.kaefik.isaifutdinov.an_weather_widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import ru.kaefik.isaifutdinov.an_weather_widget.Services.GetWeatherCityService;
import ru.kaefik.isaifutdinov.an_weather_widget.city.CityModel;

/**
 * Implementation of App Widget functionality.
 */
public class AnWeatherWidget extends AppWidgetProvider {

    public static CityModel mCityModel;

    BroadcastReceiver br;

    private TextView cityNameText;
    private TextView windText;
    private TextView tempCityText;
    private TextView timeRefreshText;
    private ImageView weatherImageView;

    // параметры для приема и передачи значений через intent
    public final static String PARAM_CITY = "city";
    public final static String PARAM_TEMP = "temp";
    public final static String PARAM_WIND = "wind";
    public final static String PARAM_TIMEREFRESH = "timeRefresh";
    public final static String PARAM_WEATHERIMAGE = "weatherImage";

    public final static String FORCE_WIDGET_UPDATE = "ru.kaefik.isaifutdinov.an_weather_widget.FORCE_WIDGET_UPDATE";

    public static String TAG_SERVICE = "AnWeatherWidget";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        String nameCity = "Apastovo";
        Intent intent;
        intent = new Intent(context, GetWeatherCityService.class);
        intent.putExtra(PARAM_CITY, nameCity);
        context.startService(intent);


        Log.i(TAG_SERVICE, "обновление виджета  updateAppWidget");


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (FORCE_WIDGET_UPDATE.equals(intent.getAction())) {

            String nameCity = intent.getStringExtra(PARAM_CITY);
            String tempCity = intent.getStringExtra(PARAM_TEMP);
            String windCity = intent.getStringExtra(PARAM_WIND);
            String timeRefreshCity = intent.getStringExtra(PARAM_TIMEREFRESH);
            String weatherImageCity = intent.getStringExtra(PARAM_WEATHERIMAGE);

            //отображение порлученных данных
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, AnWeatherWidget.class);
            int[] appWidgetId = appWidgetManager.getAppWidgetIds(thisWidget);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.an_weather_widget);
            views.setTextViewText(R.id.cityNameText, nameCity);
            views.setTextViewText(R.id.tempCityText, tempCity);
            views.setTextViewText(R.id.windText, windCity);
            views.setTextViewText(R.id.timeRefreshText, timeRefreshCity);
            Log.i(TAG_SERVICE,nameCity+" "+tempCity);
            //TODO: сделать отображение рисунка погоды
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }


    }

    @Override
    public void onEnabled(Context context) {
        mCityModel = new CityModel("Apastovo");
        mCityModel.setMYAPPID("76d6de6e46c704733f12c8738307dbb5");
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
//        context.stopService(GetWeatherCityService.class);

    }

}

