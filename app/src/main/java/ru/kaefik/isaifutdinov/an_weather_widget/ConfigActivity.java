/*
  * Copyright (C) 2016 Ilnur Saifutdinov (Ильнур Сайфутдинов)
  * e-mail: ilnursoft@gmail.com
  * MIT License
  * https://opensource.org/licenses/mit-license.php
*/

package ru.kaefik.isaifutdinov.an_weather_widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import ru.kaefik.isaifutdinov.an_weather_widget.adapter.CityModelListAdapter;

// конфигурирование виджета при помещении его на раб столе
public class ConfigActivity extends AppCompatActivity {

    private ListView mNameCity;
    private List<String> mListDataCity; // список городов
    private int mAppWidgetId; // ID текущего виджета
    private SharedPreferences mSPref;//файл настроек
    private CityModelListAdapter adapter;

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

        adapter = new CityModelListAdapter(this, mListDataCity);
        mNameCity.setAdapter(adapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG_SERVICE, "onStart  ConfigActivity");
        //-----------------------
        mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        final Context context = this;

        //-----------------------

        // Обработка события на клик по элементу списка
        mNameCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityNameString = adapter.getCityModel(position);

                saveListCity(context,cityNameString);

                Log.i(TAG_SERVICE, " OnItemClick  ConfigActivity -> выбран город " + cityNameString + "  id виджета: " + String.valueOf(mAppWidgetId));

                saveStringParametersToCfg(context, String.valueOf(mAppWidgetId), cityNameString);

                Intent resulValue = new Intent(AnWeatherWidget.CLICK_WIDGET_BUTTON);
                resulValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                //обновление виджета после отработки ConfigActivity
                try {
                    AnWeatherWidget.updateAppWidget(context, AppWidgetManager.getInstance(context), mAppWidgetId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult(RESULT_OK, resulValue);
                finish();
            }
        });
    }

    //сохранение параметра-строки  в файл параметров
    public void saveStringParametersToCfg(Context context, String parameters, String values) {
        mSPref = context.getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        if (mSPref != null) {
            SharedPreferences.Editor ed = mSPref.edit();
            ed.putString(parameters, values);
            ed.apply();
        }
    }

    // загрузка строки из файл параметров
    public static String loadStringParametersFromFile(Context context, String parameters) {
        String resSet;
        SharedPreferences mSPref = context.getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        ;
        resSet = mSPref.getString(parameters, "");
//        if (resSet == null) resSet = "";
        return resSet;
    }

    //    // сохранение списка названий городов
    public void saveListCity(Context context, String nameCity) {
        //получение списка городов которые есть
        String nameCities = loadStringParametersFromFile(context,"city");
        if(nameCities!=null) {
            nameCities+=nameCity;
        } else {
            nameCities+=","+nameCity;
        }
        saveStringParametersToCfg(context,"city",nameCities);
    }

}
