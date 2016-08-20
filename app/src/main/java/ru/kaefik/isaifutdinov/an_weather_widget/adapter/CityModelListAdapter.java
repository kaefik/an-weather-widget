
/*
  * Copyright (C) 2016 Ilnur Sayfutdinov (Ильнур Сайфутдинов)
  * e-mail: ilnursoft@gmail.com
  * MIT License
  * https://opensource.org/licenses/mit-license.php
*/


package ru.kaefik.isaifutdinov.an_weather_widget.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


import ru.kaefik.isaifutdinov.an_weather_widget.R;
import ru.kaefik.isaifutdinov.an_weather_widget.city.CityModel;

// адаптер для отображения списка городов на экране в ConfigActivity
public class CityModelListAdapter extends BaseAdapter {

    private List<String> mListcity;
    private LayoutInflater mLayoutInflater;

    public CityModelListAdapter(Context context, List<String> listcity) {
        this.mListcity = listcity;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mListcity.size();
    }

    @Override
    public Object getItem(int position) {
        return mListcity.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    //определение как будет выглядеть на экране элемент списка ListView
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.itemcity_layout, parent, false);
        }
        String cityModel = getCityModel(position);
        TextView textView = (TextView) view.findViewById(R.id.textItemCity);
//        ImageView imageWeather = (ImageView) view.findViewById(R.id.imageWeather);
        textView.setText(cityModel);
//        imageWeather.setImageURI(Uri.parse("android.resource://ru.kaefik.isaifutdinov.an_wether_prj/mipmap/" + "weather" + cityModel.getWeather("icon")));
        return view;
    }

    public String getCityModel(int position) {
        return (String) getItem(position);
    }

}
