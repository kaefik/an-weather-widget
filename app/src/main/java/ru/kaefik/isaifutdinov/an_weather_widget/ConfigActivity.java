/*
  * Copyright (C) 2016 Ilnur Saifutdinov (Ильнур Сайфутдинов)
  * e-mail: ilnursoft@gmail.com
  * MIT License
  * https://opensource.org/licenses/mit-license.php
*/

package ru.kaefik.isaifutdinov.an_weather_widget;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

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

        // Обработка события на клик по элементу списка
        mNameCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tmpCityModel = adapter.getCityModel(position);
//                try {
//                    //  startActivityForResult(tmpCityModel.putExtraIntent(getApplicationContext(), cityInfoActivity.class), RequestCode.REQUEST_CODE_CITY_WEATHER);
//                } catch (ParseException e) {
//                    // TODO: нужно обработать исключение
//                    e.printStackTrace();
//                }
            }
        });

        // Обработка долгого нажатия на элемент
        mNameCity.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//                final CityModel selectedItem = (CityModel) parent.getItemAtPosition(position);
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder.setTitle(R.string.strDeleteIte)
//                        .setMessage(getString(R.string.strDeleteCityQuestion) + selectedItem.getName() + "?")
//                        .setCancelable(false)
//                        .setPositiveButton(R.string.strDel,
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        mListDataCity.remove(position);
//                                        mNameCity.invalidateViews();
//                                        saveListCity();
//                                        saveCityInfoToFile();
//                                        Toast.makeText(getApplicationContext(), getString(R.string.strgorod) + "  " + selectedItem.getName() + " удалён.", Toast.LENGTH_SHORT).show();
//                                    }
//                                })
//                        .setNegativeButton(R.string.strOstatsya,
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                    }
//                                });
//                AlertDialog alert = builder.create();
//                alert.show();
                return true;
            }
        });

    }
}
