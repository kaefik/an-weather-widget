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
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

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
//        mCityModel = new CityModel("Kazan");
//        mCityModel.setMYAPPID("76d6de6e46c704733f12c8738307dbb5");
//        try {
//            mCityModel.getHttpWeather();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public void refreshDataView(CityModel cityModel) {

    }
}


//public class PingWidget extends AppWidgetProvider{
//    public static String FORCE_WIDGET_UPDATE = "com.example.pinger.FORCE_WIDGET_UPDATE";
//    @Override
//    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        startService(context);
//    }
//    @Override
//    public void onReceive(Context context, Intent intent){
//        super.onReceive(context, intent);
//        if (FORCE_WIDGET_UPDATE.equals(intent.getAction())) updateWidget(context);
//    }
//    private void updateWidget(Context context) {
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//        ComponentName thisWidget = new ComponentName(context, PingWidget.class);
//        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
//        drawWidget(context, appWidgetManager, appWidgetIds);
//    }
//    ...
//}
