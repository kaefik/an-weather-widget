package ru.kaefik.isaifutdinov.an_weather_widget.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ru.kaefik.isaifutdinov.an_weather_widget.AddNewCityActivity;
import ru.kaefik.isaifutdinov.an_weather_widget.AnWeatherWidget;
import ru.kaefik.isaifutdinov.an_weather_widget.city.CityModel2;
import ru.kaefik.isaifutdinov.an_weather_widget.utils.Utils;

public class GetWeatherCityService extends Service {

//    public static String AnWeatherWidget.TAG_SERVICE ="GetWeatherCityService";

    private CityModel2 mCityModel2;
    private cityInfoAsyncTask mTask;
    private int mWidgetId;


    class cityInfoAsyncTask extends AsyncTask<Void, Void, CityModel2> {
        @Override
        protected CityModel2 doInBackground(Void... voids) {

//            mCityModel2 = Utils.getHttpWeather(mCityModel2.getName());
            Log.i(AnWeatherWidget.TAG_SERVICE, "start doInBackground() -> " + mCityModel2.getName() + " -> " + mCityModel2.getCountry());
            mCityModel2 = Utils.getHttpWeather(mCityModel2.getName(), mCityModel2.getCountry());

            return mCityModel2;
        }

        @Override
        protected void onPostExecute(CityModel2 CityModel2) {
            super.onPostExecute(CityModel2);
        }
    }

    // обновление данных о погоде
    public void refreshDataWeather() throws ExecutionException, InterruptedException, TimeoutException {
        Log.i(AnWeatherWidget.TAG_SERVICE, "start refreshDataWeather()");
        if (mTask != null) {
            mTask.cancel(true);
        }
        mTask = new cityInfoAsyncTask();

        Log.i(AnWeatherWidget.TAG_SERVICE, "mTask.execute()");
        mTask.execute();
        mCityModel2 = mTask.get(5, TimeUnit.SECONDS);
        saveCityInfoToFile(mCityModel2);
    }


    public GetWeatherCityService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(AnWeatherWidget.TAG_SERVICE, "Start service GetWeatherCityService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(AnWeatherWidget.TAG_SERVICE, "Start onStartCommand  GetWeatherCityService");
        mCityModel2 = new CityModel2(intent.getStringExtra(AnWeatherWidget.PARAM_CITY), intent.getStringExtra(AnWeatherWidget.PARAM_COUNTRY));
//        mCityModel2.setMYAPPID("76d6de6e46c704733f12c8738307dbb5");
        // получение данных через intent: имя города который нужно обновить
//        mCityModel2.setName(intent.getStringExtra(AnWeatherWidget.PARAM_CITY));
        mWidgetId = intent.getIntExtra(AnWeatherWidget.PARAM_WIDGETID, 0);

        if (Utils.isConnected(getApplication())) {
            Log.i(AnWeatherWidget.TAG_SERVICE, "Start onStartCommand  GetWeatherCityService -> интернет есть");
            //обновление погоды
            try {
                refreshDataWeather();
            } catch (ExecutionException e) {
                Toast.makeText(getApplicationContext(), "Ошибка обновления (ExecutionException)", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (InterruptedException e) {
                Toast.makeText(getApplicationContext(), "Ошибка обновления (InterruptedException)", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (TimeoutException e) {
                Toast.makeText(getApplicationContext(), "Ошибка обновления (TimeoutException)", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            Log.i(AnWeatherWidget.TAG_SERVICE, "после refreshDataWeather() имя города: " + mCityModel2.getName() + " " + mCityModel2.getCountry() + " -> " + mCityModel2.getTemp());

            Log.i(AnWeatherWidget.TAG_SERVICE, "start refreshWidget");
            if (!mCityModel2.isEmptyWeatherDescription()) {
                refreshWidget();
            }
        } else {
            Log.i(AnWeatherWidget.TAG_SERVICE, "Start onStartCommand  GetWeatherCityService -> интернета НЕТ");
        }

        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(AnWeatherWidget.TAG_SERVICE, "Destroy GetWeatherCityService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // обновление виджета используя широковещательные сообщения
    private void refreshWidget() {
        Log.i(AnWeatherWidget.TAG_SERVICE, "refreshWidget имя города: " + mCityModel2.getName() + " -> " + mCityModel2.getTemp() + " mWidgetId = " + String.valueOf(mWidgetId));
        // отправка виджету погоды данные о погоде
        Intent intent = new Intent(AnWeatherWidget.FORCE_WIDGET_UPDATE);
        intent.putExtra(AnWeatherWidget.PARAM_CITY, mCityModel2.getName());
        intent.putExtra(AnWeatherWidget.PARAM_COUNTRY, mCityModel2.getCountry());
        intent.putExtra(AnWeatherWidget.PARAM_TEMP, Float.toString(mCityModel2.getTemp()) + "C");
        intent.putExtra(AnWeatherWidget.PARAM_TIMEREFRESH, mCityModel2.getTimeRefresh());
        intent.putExtra(AnWeatherWidget.PARAM_DESCWEATHER, mCityModel2.getWeatherDescription());  //getWeather("description"));
        intent.putExtra(AnWeatherWidget.PARAM_WEATHERIMAGE, mCityModel2.getWeatherIcon());  //getWeather("icon"));
        intent.putExtra(AnWeatherWidget.PARAM_WIND, Utils.windGradus2Rumb(mCityModel2.getWinddirection()) + " (" + Float.toString(mCityModel2.getWindspeed()) + " м/с)");
        intent.putExtra(AnWeatherWidget.PARAM_WIDGETID, mWidgetId);
//        saveCityInfoToFile(mCityModel2);
        sendBroadcast(intent);
    }


    @Override
    public boolean stopService(Intent name) {
        Log.i(AnWeatherWidget.TAG_SERVICE, "Stop service GetWeatherCityService");
        return super.stopService(name);
    }

    // проверка на то что namecity есть в списке имен городов, true - есть, false - нет
    public boolean isExistNameFromList(List<CityModel2> listcity, String namecity) {
        boolean flagExistNameCity = false;
        for (int i = 0; i < listcity.size(); i++) {
            if (listcity.get(i).getName().equals(namecity)) {
                flagExistNameCity = true;
                break;
            }
        }
        return flagExistNameCity;
    }

    //    восстановление сохраненых данных о погоде(каждый город-отдельный файл с Josn)
    public static CityModel2 restoreCityInfoFromFile(Context context, CityModel2 CityModel2) throws JSONException {
        String nameFile = CityModel2.getName() + "-" + CityModel2.getCountry();
        if (nameFile != null) {
            Log.i(AddNewCityActivity.TAG_SERVICE, "restoreCityInfoFromFile :  nameFile  " + nameFile);
            if (!nameFile.equals("")) {
                CityModel2.openFile(nameFile + ".txt", context);
            }
        }
        return CityModel2;
    }

    //сохранение данных о погоде каждый город в отдельный файл
    public void saveCityInfoToFile(CityModel2 CityModel2) {
        String nameFile = CityModel2.getName() + "-" + CityModel2.getCountry();
        if (nameFile != null) {
            try {
                CityModel2.saveToFile(nameFile + ".txt", getApplicationContext());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
