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
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import org.json.JSONException;

import ru.kaefik.isaifutdinov.an_weather_widget.Services.GetWeatherCityService;
import ru.kaefik.isaifutdinov.an_weather_widget.city.CityModel2;
import ru.kaefik.isaifutdinov.an_weather_widget.utils.Utils;

import static android.content.Context.MODE_PRIVATE;


public class AnWeatherWidget extends AppWidgetProvider {

    // параметры для приема и передачи значений через intent
    public final static String PARAM_CITY = "city";
    //    public final static String PARAM_APPID = "appID";
    public final static String PARAM_TEMP = "temp";
    public final static String PARAM_WIND = "wind";
    public final static String PARAM_TIMEREFRESH = "timeRefresh";
    public final static String PARAM_WEATHERIMAGE = "weatherImage";
    public final static String PARAM_DESCWEATHER = "descriptionWeather";
    public final static String PARAM_WIDGETID = "widgetId";
    public final static String PARAM_COUNTRY = "country";

    // действие принудилельного обновления данных виджета
    public final static String FORCE_WIDGET_UPDATE = "ru.kaefik.isaifutdinov.an_weather_widget.FORCE_WIDGET_UPDATE";
    // действие на нажатие кпонки на виджете (обновление данных)
    public final static String CLICK_WIDGET_BUTTON = "ru.kaefik.isaifutdinov.an_weather_widget.CLICK_WIDGET_BUTTON";

    public static String TAG_SERVICE = "AnWeatherWidget";
    public static final String WIDGET_PREF = "anweatherwidgetconfig";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) throws JSONException {

        Log.i(TAG_SERVICE, "start обновление виджета  updateAppWidget" + "  appWidgetId = " + String.valueOf(appWidgetId));

        String nameCity = AddNewCityActivity.loadStringParametersFromFile(context, String.valueOf(appWidgetId));
        CityModel2 mCityModel2 = new CityModel2(Utils.getNameCity(nameCity), Utils.getCountry(nameCity));


        //получение данных из сохраненного файла
        //TODO: разобраться с восстановлением города из файла
        mCityModel2 = GetWeatherCityService.restoreCityInfoFromFile(context, mCityModel2);
        Log.i(TAG_SERVICE, "updateAppWidget  ->  восстановлен из файла " + mCityModel2.getName() + " -> " + mCityModel2.getCountry() + " -> " + mCityModel2.getTemp());

        if (!nameCity.trim().equals("")) {
            Log.i(TAG_SERVICE, "обновление виджета  updateAppWidget - >город: " + Utils.getCountry(nameCity) + " " + Utils.getNameCity(nameCity) + "  appWidgetId = " + String.valueOf(appWidgetId) + "!nameCity.trim().equals");
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
            remoteViews.setTextViewText(R.id.cityNameText, mCityModel2.getName() + "(" + mCityModel2.getCountry() + ")");
            remoteViews.setTextViewText(R.id.tempCityText, mCityModel2.getTemp() + "C");
            remoteViews.setTextViewText(R.id.windText, Utils.windGradus2Rumb(mCityModel2.getWinddirection()) + " (" + Float.toString(mCityModel2.getWindspeed()) + " м/с)");
            remoteViews.setTextViewText(R.id.timeRefreshText, mCityModel2.getTimeRefresh());
            remoteViews.setTextViewText(R.id.descriptionWeatherText, mCityModel2.getWeatherDescription()); //  getWeather("description"));
            remoteViews.setImageViewUri(R.id.weatherImageView, Uri.parse("android.resource://ru.kaefik.isaifutdinov.an_weather_widget/mipmap/" + "weather" + mCityModel2.getWeatherIcon())); //getWeather("icon")));

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            // END - вешаем на кпонку событие CLICK_WIDGET_BUTTON чтобы его обработать в методе onReceive

            startGetWeatherCityService(context, appWidgetId, mCityModel2);

        } else {
            Log.i(TAG_SERVICE, "обновление виджета  updateAppWidget - > пустой город" + "  appWidgetId = " + String.valueOf(appWidgetId));
        }
    }

    // запуск сервиса GetWeatherCityService обновления данных города
    public static void startGetWeatherCityService(Context context, int appWidgetId, CityModel2 CityModel2) {
        Intent intent;
        Log.i(TAG_SERVICE, "зашли в метод startGetWeatherCityService " + CityModel2.getName() + " -> " + CityModel2.getCountry());
        intent = new Intent(context, GetWeatherCityService.class);
        intent.putExtra(PARAM_CITY, CityModel2.getName());
        intent.putExtra(PARAM_COUNTRY, CityModel2.getCountry());
        intent.putExtra(PARAM_WIDGETID, appWidgetId);
        context.startService(intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            try {
                updateAppWidget(context, appWidgetManager, appWidgetId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG_SERVICE, "зашли в метод onReceive " + intent.getAction());
        super.onReceive(context, intent);
        if (FORCE_WIDGET_UPDATE.equals(intent.getAction())) {
            String nameCity = intent.getStringExtra(PARAM_CITY);
            String tempCity = intent.getStringExtra(PARAM_TEMP);
            String windCity = intent.getStringExtra(PARAM_WIND);
            String timeRefreshCity = intent.getStringExtra(PARAM_TIMEREFRESH);
            String weatherImageCity = intent.getStringExtra(PARAM_WEATHERIMAGE);
            String descriptionWeather = intent.getStringExtra(PARAM_DESCWEATHER);
            String country = intent.getStringExtra(PARAM_COUNTRY);
            int WidgetId = intent.getIntExtra(PARAM_WIDGETID, 0);

            //TODO: проверить что данные поступили нулевые, тогда получаем данные из файла ИМЯГОРОДА. Возможно это перенести в GetWeatherCityService

            Log.i(TAG_SERVICE, "onReceive " + nameCity + " " + country + " -> " + tempCity);

            //отображение порлученных данных
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, AnWeatherWidget.class);
            int[] appWidgetId = appWidgetManager.getAppWidgetIds(thisWidget);

            for (int i = 0; i < appWidgetId.length; i++) {
                Log.i(TAG_SERVICE, "onReceive ->  appWidgetId = " + String.valueOf(appWidgetId[i]));
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.an_weather_widget);
                if (WidgetId == appWidgetId[i]) {
                    views.setTextViewText(R.id.cityNameText, nameCity + "(" + country + ")");
                    views.setTextViewText(R.id.tempCityText, tempCity);
                    views.setTextViewText(R.id.windText, windCity);
                    views.setTextViewText(R.id.timeRefreshText, timeRefreshCity);
                    views.setTextViewText(R.id.descriptionWeatherText, descriptionWeather);
                    views.setImageViewUri(R.id.weatherImageView, Uri.parse("android.resource://ru.kaefik.isaifutdinov.an_weather_widget/mipmap/" + "weather" + weatherImageCity));

                    // вешаем на кнопку событие CLICK_WIDGET_BUTTON чтобы его обработать в методе onReceive
                    //Подготавливаем Intent для Broadcast
                    Intent active = new Intent(context, AnWeatherWidget.class);
                    active.setAction(CLICK_WIDGET_BUTTON);
                    //создаем наше событие
                    PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
                    //регистрируем наше событие
                    views.setOnClickPendingIntent(R.id.refreshButton, actionPendingIntent);
                    //обновляем виджет
                    // END - вешаем на кпонку событие CLICK_WIDGET_BUTTON чтобы его обработать в методе onReceive
                    appWidgetManager.updateAppWidget(appWidgetId[i], views);
                    break;
                }
                appWidgetManager.updateAppWidget(appWidgetId[i], views);
            }
        }
        if (CLICK_WIDGET_BUTTON.equals(intent.getAction())) {
            Log.i(TAG_SERVICE, "нажали кнопку обновление данных");

            //отображение полученных данных
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, AnWeatherWidget.class);
            int[] appWidgetId = appWidgetManager.getAppWidgetIds(thisWidget);


            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.an_weather_widget);
            // обновление всех наших виджетов
            for (int i = 0; i < appWidgetId.length; i++) {
                String nameCity = AddNewCityActivity.loadStringParametersFromFile(context, String.valueOf(appWidgetId[i]));
                Log.i(TAG_SERVICE, "id виждета при обновлении виджетов -> " + String.valueOf(appWidgetId[i] + " город " + nameCity));
                startGetWeatherCityService(context, appWidgetId[i], new CityModel2(Utils.getNameCity(nameCity), Utils.getCountry(nameCity)));
            }
        }
    }

    @Override
    public void onEnabled(Context context) {
        //TODO: сделать запуск activity чтобы выбрать текущий город для виджета
//        mCityModel2 = new CityModel2("Kazan");
//        mCityModel2.setMYAPPID("76d6de6e46c704733f12c8738307dbb5");
        Log.i(TAG_SERVICE, "onEnabled Widget");
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        // Удаляем Preferences
        SharedPreferences mSPref = context.getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSPref.edit();
        for (int widgetID : appWidgetIds) {
            editor.remove(String.valueOf(widgetID));
        }
        editor.commit();
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


}

