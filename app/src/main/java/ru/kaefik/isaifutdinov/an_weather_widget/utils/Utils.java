/*
  * Copyright (C) 2016 Ilnur Saifutdinov (Ильнур Сайфутдинов)
  * e-mail: ilnursoft@gmail.com
  * MIT License
  * https://opensource.org/licenses/mit-license.php
*/

package ru.kaefik.isaifutdinov.an_weather_widget.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    static Map mTranslateWeather = new HashMap<String, String>();

    // получение страницы из урла strurl
    static public String getHttpRequestFromUrl(String strurl) {
        String resultStr = "";
        try {
            URL url = new URL(strurl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            InputStream stream = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            resultStr = "";
            while ((line = bufferedReader.readLine()) != null) {
                resultStr += line;
            }
            bufferedReader.close();
            return resultStr;
        } catch (MalformedURLException e) {
//            e.printStackTrace();
            resultStr = "";
        } catch (IOException e) {
//            e.printStackTrace();
            resultStr = "";
        }
        return resultStr;
    }

    // получение объектов из json
    static public String getObjFromJson(String sjosn, String nameParrent, String nameChild) {

        JSONObject parentObject = null;
        JSONObject childObject = null;
        String res = null;
        try {
            parentObject = new JSONObject(sjosn);
            if (nameParrent != null) {
                res = parentObject.get(nameParrent).toString();
                if (nameChild != null) {
                    if (res != null) {
                        childObject = new JSONObject(res);
                        res = childObject.get(nameChild).toString();
                    }
                }
            }
        } catch (JSONException e) {
            res = "0";
        }
        return res;
    }

    //сделать первую букву заглавной
    public static String firstUpCaseString(String word) {
        if (word == null || word.isEmpty()) return "";
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }


    //----- работа  с файлами

    // Метод для открытия файла
    public static String openFile(String filename, Context context) {
        String res = "";
        try {
            FileInputStream inputStream = context.openFileInput(filename);
            if (inputStream != null) {
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isr);
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                inputStream.close();
                res = builder.toString();
            }
        } catch (Throwable t) {
            // TODO: сделать передачу исключения к уровню повыше, чтобы вывести пользователю сообщение об ошибке
        }
        return res;
    }


    // Метод для сохранения файла
    public static void saveFile(String filename, String strText, Context context) {
        try {
            FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(outputStream);
            osw.write(strText);
            osw.close();
        } catch (Throwable t) {
            //TODO: сделать передачу исключения к уровню повыше, чтобы вывести пользователю сообщение об ошибке
        }
    }


    //----- END работа  с файлами


    // перевод градусов в румбы (С, В, Ю, З и т д)
    public static String windGradus2Rumb(Float gradusWind) {
        String windRumb = "";

        if ((gradusWind < 0) | (gradusWind > 360)) {
            windRumb = "";
        }

        if ((gradusWind >= 0) & (gradusWind <= 45)) {
            gradusWind = Utils.getNearNumber(gradusWind, 0f, 45f);
        }
        if ((gradusWind >= 45) & (gradusWind <= 90)) {
            gradusWind = Utils.getNearNumber(gradusWind, 45f, 90f);
        }
        if ((gradusWind >= 90) & (gradusWind <= 135)) {
            gradusWind = Utils.getNearNumber(gradusWind, 90f, 135f);
        }
        if ((gradusWind >= 135) & (gradusWind <= 180)) {
            gradusWind = Utils.getNearNumber(gradusWind, 135f, 180f);
        }
        if ((gradusWind >= 180) & (gradusWind <= 225)) {
            gradusWind = Utils.getNearNumber(gradusWind, 180f, 225f);
        }
        if ((gradusWind >= 225) & (gradusWind <= 270)) {
            gradusWind = Utils.getNearNumber(gradusWind, 225f, 270f);
        }
        if ((gradusWind >= 270) & (gradusWind <= 315)) {
            gradusWind = Utils.getNearNumber(gradusWind, 270f, 315f);
        }
        if ((gradusWind >= 315) & (gradusWind <= 360)) {
            gradusWind = Utils.getNearNumber(gradusWind, 315f, 360f);
        }

        if ((gradusWind == 0f) | (gradusWind == 360f)) {
            windRumb = "С";
        }
        if (gradusWind == 45f) {
            windRumb = "СВ";
        }
        if (gradusWind == 90f) {
            windRumb = "В";
        }
        if (gradusWind == 135f) {
            windRumb = "ЮВ";
        }
        if (gradusWind == 180f) {
            windRumb = "Ю";
        }
        if (gradusWind == 225f) {
            windRumb = "ЮЗ";
        }
        if (gradusWind == 270f) {
            windRumb = "З";
        }
        if (gradusWind == 315f) {
            windRumb = "СЗ";
        }
        return windRumb;

    }

    // возвращает ближайшее из двух чисел ch1 или ch2 число ch
    public static Float getNearNumber(Float ch, Float ch1, Float ch2) {
        Float d = Math.abs(ch2 - ch1) / 2;
        if (ch <= ch1 + d) {
            return ch1;
        } else {
            return ch2;
        }

    }

    // создание перевода описания погоды с английского на русский
    // (http://openweathermap.org/weather-conditions)
    public static void createTranslateWeatherDescription() {
        mTranslateWeather.put("clear sky", "ясно");
        mTranslateWeather.put("few clouds", "малооблачно");
        mTranslateWeather.put("scattered clouds", "малооблачно");
        mTranslateWeather.put("broken clouds", "облачность с просветами");
        mTranslateWeather.put("shower rain", "ливень");
        mTranslateWeather.put("rain", "дождь");
        mTranslateWeather.put("thunderstorm", "гроза");
        mTranslateWeather.put("snow", "снег");
        mTranslateWeather.put("mist", "туман");
        //----Rain
        mTranslateWeather.put("light rain", "легкий дождь");
        mTranslateWeather.put("moderate rain", "небольшой дождь");
        mTranslateWeather.put("heavy intensity rain", "сильный дождь");
        mTranslateWeather.put("very heavy rain", "очень сильный дождь");
        mTranslateWeather.put("extreme rain", "экстремальный дождь");
        mTranslateWeather.put("freezing rain", "ледяной дождь");
        mTranslateWeather.put("light intensity shower rain", "легкий ливень");
        mTranslateWeather.put("shower rain", "ливень");
        mTranslateWeather.put("heavy intensity shower rain", "сильный ливень");
        mTranslateWeather.put("ragged shower rain", "ливень стеной");
        //----Snow
        mTranslateWeather.put("light snow ", "легкий снег");
        mTranslateWeather.put("heavy snow", "снегопад");
        mTranslateWeather.put("sleet", "дождь со снегом");
        mTranslateWeather.put("shower sleet", "дождь со снегом");
        mTranslateWeather.put("light rain and snow", "легкий дождь со снегом");
        mTranslateWeather.put("rain and snow", "дождь со снегом");
        mTranslateWeather.put("light shower snow", "легкий снег");
        mTranslateWeather.put("shower snow", "снег");
        mTranslateWeather.put("heavy shower snow", "тяжелый снегопад");
        //----Atmosphere
        mTranslateWeather.put("smoke", "смок");
        mTranslateWeather.put("haze", "дамка");
        mTranslateWeather.put("sand, dust whirls", "песок");
        mTranslateWeather.put("fog", "туман");
        mTranslateWeather.put("sand", "песок");
        mTranslateWeather.put("dust", "пыль");
        mTranslateWeather.put("volcanic ash", "вулканический пепел");
        mTranslateWeather.put("squalls", "шквалы ветра");
        mTranslateWeather.put("tornado", "торнадо");
        //----Clouds
        mTranslateWeather.put("overcast clouds", "пасмурно");
        //----Thunderstorm
        mTranslateWeather.put("thunderstorm with light rain", "легкий дождь с грозой");
        mTranslateWeather.put("thunderstorm with rain", "дождь с грозой");
        mTranslateWeather.put("thunderstorm with heavy rain", "сильный дождь с грозой");
        mTranslateWeather.put("light thunderstorm", "легкая гроза");
        mTranslateWeather.put("thunderstorm", "гроза");
        mTranslateWeather.put("heavy thunderstorm", "сильная гроза");
        mTranslateWeather.put("ragged thunderstorm", "сильная гроза");
        mTranslateWeather.put("thunderstorm with light drizzle", "гроза с легкой изморосью");
        mTranslateWeather.put("thunderstorm with drizzle", "мелкий дождь с грозой");
        mTranslateWeather.put("thunderstorm with heavy drizzle", "гроза с сильной изморозью");
        //----Drizzle
        mTranslateWeather.put("light intensity drizzle", "легкая изморось");
        mTranslateWeather.put("drizzle", "изморось");
        mTranslateWeather.put("heavy intensity drizzle", "сильная изморось");
        mTranslateWeather.put("light intensity drizzle rain", "дождь с изморосью");
        mTranslateWeather.put("drizzle rain", "изморось с дождем");
        mTranslateWeather.put("heavy intensity drizzle rain", "сильный дождь с изморосью");
        mTranslateWeather.put("shower rain and drizzle", "ливень с изморосью");
        mTranslateWeather.put("heavy shower rain and drizzle", "сильный ливень с изморосью");
        mTranslateWeather.put("shower drizzle", "крупная изморось");
    }


    // перевод описания погоды с английского на русский
    // (http://openweathermap.org/weather-conditions)
    public static String translateWeatherDescription(String descWeather) {
        //--------
        if (mTranslateWeather.get(descWeather) != null) {
            return mTranslateWeather.get(descWeather).toString();
        } else {
            return "неизвестно";
        }
    }

    // проверка на наличение подключения к интернет.  true - сервисы интернета подключены
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {
            return true;
        }
        return false;
    }

}
