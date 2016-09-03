/*
  * Copyright (C) 2016 Ilnur Saifutdinov (Ильнур Сайфутдинов)
  * e-mail: ilnursoft@gmail.com
  * MIT License
  * https://opensource.org/licenses/mit-license.php
*/

package ru.kaefik.isaifutdinov.an_weather_widget;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import ru.kaefik.isaifutdinov.an_weather_widget.adapter.CityModelListAdapter;
import ru.kaefik.isaifutdinov.an_weather_widget.city.CityModel;

// конфигурирование виджета при помещении его на раб столе
public class ConfigActivity extends AppCompatActivity {

    private ListView mNameCity;
    List<String> mListDataCity; // список городов
    public int mAppWidgetId;
    private SharedPreferences mSPref;

    public static String TAG_SERVICE = "AnWeatherWidget";
    public static final String WIDGET_PREF = "anweatherwidgetconfig";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG_SERVICE, "onCreate  ConfigActivity");
        setContentView(R.layout.activity_config);

        setResult(RESULT_CANCELED);

        mNameCity = (ListView) findViewById(R.id.cityListView);

        // наполнение списка городов
        mListDataCity = new ArrayList<String>();
        mListDataCity.add("Kazan");
        mListDataCity.add("Moscow");
        mListDataCity.add("Samara");
        mListDataCity.add("Istanbul");
        mListDataCity.add("London");
        mListDataCity.add("Apastovo");

        final CityModelListAdapter adapter = new CityModelListAdapter(this, mListDataCity);
        mNameCity.setAdapter(adapter);


        //-----------------------
        mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        final AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
//        final RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.an_weather_widget);
        final Context context = this;

        //-----------------------

        // Обработка события на клик по элементу списка
        mNameCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityNameString = adapter.getCityModel(position);

                Log.i(TAG_SERVICE, " OnItemClick  ConfigActivity -> выбран город " + cityNameString + "  id виджета: " + String.valueOf(mAppWidgetId));

                saveStringParametersToCfg(String.valueOf(mAppWidgetId), cityNameString);
                Log.i(TAG_SERVICE, "");

//                AnWeatherWidget.updateAppWidget(context, AppWidgetManager.getInstance(context), mAppWidgetId);

//                // вешаем на кпонку событие CLICK_WIDGET_BUTTON чтобы его обработать в методе onReceive
//                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.an_weather_widget);
//                //Подготавливаем Intent для Broadcast
//                Intent active = new Intent(context, AnWeatherWidget.class);
//                active.setAction(AnWeatherWidget.CLICK_WIDGET_BUTTON);
//                //создаем наше событие
//                PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
//                //регистрируем наше событие
//                remoteViews.setOnClickPendingIntent(R.id.refreshButton, actionPendingIntent);
//                //обновляем виджет
//                widgetManager.updateAppWidget(mAppWidgetId, remoteViews);
////                sendBroadcast(active);

                // END - вешаем на кпонку событие CLICK_WIDGET_BUTTON чтобы его обработать в методе onReceive

                Intent resulValue = new Intent(AnWeatherWidget.CLICK_WIDGET_BUTTON);
                resulValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                //обновление виджета после отработки ConfigActivity
//                AnWeatherWidget.updateAppWidget(context, widgetManager,mAppWidgetId);
                AnWeatherWidget.updateAppWidget(context, AppWidgetManager.getInstance(context),mAppWidgetId);
                setResult(RESULT_OK, resulValue);
                finish();
            }
        });
    }


    //сохранение параметра-строки  в файл параметров
    public void saveStringParametersToCfg(String parameters, String values) {
        mSPref = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        if (mSPref != null) {
            SharedPreferences.Editor ed = mSPref.edit();
            ed.putString(parameters, values);
            ed.apply();
        }
    }

}
