/*
  * Copyright (C) 2016 Ilnur Saifutdinov (Ильнур Сайфутдинов)
  * e-mail: ilnursoft@gmail.com
  * MIT License
  * https://opensource.org/licenses/mit-license.php
*/


package ru.kaefik.isaifutdinov.an_weather_widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import ru.kaefik.isaifutdinov.an_weather_widget.Services.GetWeatherCityService;
import ru.kaefik.isaifutdinov.an_weather_widget.city.CityModel;
import ru.kaefik.isaifutdinov.an_weather_widget.utils.Utils;

public class AnWeatherWidget extends AppWidgetProvider {

    private CityModel mCityModel;

    // параметры для приема и передачи значений через intent
    public final static String PARAM_CITY = "city";
    //    public final static String PARAM_APPID = "appID";
    public final static String PARAM_TEMP = "temp";
    public final static String PARAM_WIND = "wind";
    public final static String PARAM_TIMEREFRESH = "timeRefresh";
    public final static String PARAM_WEATHERIMAGE = "weatherImage";
    public final static String PARAM_DESCWEATHER = "descriptionWeather";

    // действие принудилельного обновления данных виджета
    public final static String FORCE_WIDGET_UPDATE = "ru.kaefik.isaifutdinov.an_weather_widget.FORCE_WIDGET_UPDATE";
    // действие на нажатие кпонки на виджете (обновление данных)
    public final static String CLICK_WIDGET_BUTTON = "ru.kaefik.isaifutdinov.an_weather_widget.CLICK_WIDGET_BUTTON";

    public static String TAG_SERVICE = "AnWeatherWidget";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // вешаем на кпонку событие CLICK_WIDGET_BUTTON чтобы его обработать в методе onReceive
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.an_weather_widget);
        //Подготавливаем Intent для Broadcast
        Intent active = new Intent(context, AnWeatherWidget.class);
        active.setAction(CLICK_WIDGET_BUTTON);
        //создаем наше событие
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
        //регистрируем наше событие
        remoteViews.setOnClickPendingIntent(R.id.refreshButton, actionPendingIntent);
        //обновляем виджет
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        // END - вешаем на кпонку событие CLICK_WIDGET_BUTTON чтобы его обработать в методе onReceive

        String nameCity = "Kazan";
        startGetWeatherCityService(context,new CityModel(nameCity));
//        Intent intent;
//        intent = new Intent(context, GetWeatherCityService.class);
//        intent.putExtra(PARAM_CITY, nameCity);
//        context.startService(intent);


        Log.i(TAG_SERVICE, "обновление виджета  updateAppWidget");


    }

    // запуск сервиса GetWeatherCityService обновления данных города
    public static void startGetWeatherCityService(Context context, CityModel cityModel) {
        Intent intent;
        intent = new Intent(context, GetWeatherCityService.class);
        intent.putExtra(PARAM_CITY, cityModel.getName());
        context.startService(intent);
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
            String descriptionWeather = intent.getStringExtra(PARAM_DESCWEATHER);

            Log.i(TAG_SERVICE, "onReceive " + nameCity + " -> " + tempCity);

            //отображение порлученных данных
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, AnWeatherWidget.class);
            int[] appWidgetId = appWidgetManager.getAppWidgetIds(thisWidget);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.an_weather_widget);
            views.setTextViewText(R.id.cityNameText, nameCity);
            views.setTextViewText(R.id.tempCityText, tempCity);
            views.setTextViewText(R.id.windText, windCity);
            views.setTextViewText(R.id.timeRefreshText, timeRefreshCity);
            views.setTextViewText(R.id.descriptionWeatherText, descriptionWeather);
            //TODO: сделать отображение рисунка погоды
            views.setImageViewUri(R.id.weatherImageView, Uri.parse("android.resource://ru.kaefik.isaifutdinov.an_weather_widget/mipmap/" + "weather" + weatherImageCity));
//            imageWeather.setImageURI();
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        if (CLICK_WIDGET_BUTTON.equals(intent.getAction())) {
            Log.i(TAG_SERVICE, "нажали кнопку обновление данных");

            String nameCity = "Kazan";
            startGetWeatherCityService(context,new CityModel(nameCity));

        }


    }

    @Override
    public void onEnabled(Context context) {
        //TODO: сделать запуск activity чтобы выбрать текущий город для виджета
//        mCityModel = new CityModel("Kazan");
//        mCityModel.setMYAPPID("76d6de6e46c704733f12c8738307dbb5");
        Utils.createTranslateWeatherDescription();
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

