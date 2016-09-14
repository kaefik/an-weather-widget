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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import ru.kaefik.isaifutdinov.an_weather_widget.adapter.CityModelListAdapter;
import ru.kaefik.isaifutdinov.an_weather_widget.utils.Utils;

// конфигурирование виджета при помещении его на раб столе
public class ConfigActivity extends AppCompatActivity {

    private ListView mNameCity;

    //добавить элемент nameCity  в поле mListDataCity
    public void addListDataCity(String nameCity) {
        if (mListDataCity == null) mListDataCity = new ArrayList<String>();
        mListDataCity.add(nameCity);
    }

    private List<String> mListDataCity; // список городов
    private int mAppWidgetId; // ID текущего виджета
    private SharedPreferences mSPref;//файл настроек
    private CityModelListAdapter adapter;
    private EditText nameCityEditText;

    public static String TAG_SERVICE = "AnWeatherWidget";
    public static final String WIDGET_PREF = "anweatherwidgetconfig";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG_SERVICE, "onCreate  ConfigActivity");
        setContentView(R.layout.activity_config);

        nameCityEditText = (EditText) findViewById(R.id.nameCityEditText);
        ;

        setResult(RESULT_CANCELED);

        mNameCity = (ListView) findViewById(R.id.cityListView);
        Log.i(TAG_SERVICE, "onCreate  ConfigActivity  start  loadListCity");
        loadListCity();
        Log.i(TAG_SERVICE, "onCreate  ConfigActivity  after  loadListCity");

//        saveListCity();

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

//                addListDataCity(cityNameString);
//
//                saveListCity();

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
        if (resSet == null) resSet = "";
        return resSet;
    }

//    //    // сохранение списка названий городов
//    public void saveListCity(Context context, String nameCity) {
//        //получение списка городов которые есть
//        String nameCities = loadStringParametersFromFile(context,"city");
//        if(nameCities!=null) {
//            nameCities+=nameCity;
//        } else {
//            nameCities+=","+nameCity;
//        }
//        saveStringParametersToCfg(context,"city",nameCities);
//    }


    // добавления нового города
    public void onClickAddCity(View v) {
        String newCity = Utils.firstUpCaseString(nameCityEditText.getText().toString().trim());
        // TODO: СЮДА ДОБАВИТЬ ДОПОЛНИТЕЛЬНЫЕ ПРОВЕРКИ ВВОДА НАЗВАНИЯ ГОРОДА
        if (!newCity.equals("")) {
            mListDataCity.add(newCity);
            Toast.makeText(getApplicationContext(), newCity, Toast.LENGTH_SHORT).show();
        }
        nameCityEditText.setText("");
        // прячем клавиатуру
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(nameCityEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
//        startcityInfoAsyncTask(mListDataCity);
        saveListCity();
//        saveCityInfoToFile();
    }

    // сохранение списка названий городов
    public void saveListCity() {
        String stringCityName = "";
        for (int i = 0; i < mListDataCity.size(); i++) {
            stringCityName += mListDataCity.get(i) + ",";
        }
        saveStringParametersToCfg(this, "city", stringCityName);
//        saveCityParameters("city", stringCityName);
    }

    // загрузка списка названий городов
    public void loadListCity() {
        Log.i(TAG_SERVICE, "loadListCity()");
        String stringCityName = "";
        stringCityName = loadStringParametersFromFile(this, "city");
        Log.i(TAG_SERVICE, "loadListCity()   -> " + stringCityName);
        if (!stringCityName.trim().equals("")) {
            String stringListCityNames[] = stringCityName.split(",");
            if (mListDataCity == null) mListDataCity = new ArrayList<String>();
            mListDataCity.clear();
            for (int i = 0; i < stringListCityNames.length; i++) {
                mListDataCity.add(i, stringListCityNames[i]);
            }
        }
        // если нет ранее сохраненных городов или произошла ошибка чтения сохранненых городов,
        // то загружаются список по умолчанию
        if (mListDataCity == null) {
            mListDataCity = new ArrayList<String>();
            mListDataCity.add("Kazan");
            mListDataCity.add("Moscow");
            mListDataCity.add("Samara");
            mListDataCity.add("Istanbul");
            mListDataCity.add("London");
            mListDataCity.add("Apastovo");
//            Toast.makeText(getApplicationContext(), R.string.strDwnloadCityDefault, Toast.LENGTH_SHORT);
        }
        mNameCity.invalidateViews();
    }


}
