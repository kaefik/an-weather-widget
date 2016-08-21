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
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ru.kaefik.isaifutdinov.an_weather_widget.adapter.CityModelListAdapter;
import ru.kaefik.isaifutdinov.an_weather_widget.city.CityModel;

// конфигурирование виджета при помещении его на раб столе
public class ConfigActivity extends AppCompatActivity {

    private ListView mNameCity;
    List<String> mListDataCity; // список городов
    public  int mAppWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
//            showProgressDialog();
//            saveTheUserValueInPref(selectedCategory, sourceAndLanguage,
//                    mAppWidgetId);
//            getDataToLoadInWidget = new GetDataToLoadInWidget(
//                    ConfigurationActivity.this, selectedSource,
//                    selectedLanguage, selectedCategory);
        }

        final AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
        final RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.an_weather_widget);

//        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
//            finish();
//        }
        //-----------------------

        // Обработка события на клик по элементу списка
        mNameCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityNameString = adapter.getCityModel(position);
//                 final String selectedItem = (String) parent.getItemAtPosition(position);
//                Intent intent = new Intent(Intent.ACTION_VIEW,selectedItem);
//                PendingIntent pending = PendingIntent.getActivity(this,0,intent,0);
                remoteViews.setTextViewText(R.id.cityNameText,cityNameString);
                widgetManager.updateAppWidget(mAppWidgetId,remoteViews);

                Intent resulValue = new Intent();
                resulValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,mAppWidgetId);
                setResult(RESULT_OK,resulValue);
                finish();
//
            }
        });

        // Обработка долгого нажатия на элемент
//        mNameCity.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//
//            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//                return true;
//            }
//        }
//    );

    }
}
