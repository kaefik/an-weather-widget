package ru.kaefik.isaifutdinov.an_weather_widget.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ru.kaefik.isaifutdinov.an_weather_widget.AnWeatherWidget;
import ru.kaefik.isaifutdinov.an_weather_widget.city.CityModel;
import ru.kaefik.isaifutdinov.an_weather_widget.utils.Utils;

public class GetWeatherCityService extends Service {

//    public static String AnWeatherWidget.TAG_SERVICE ="GetWeatherCityService";

    private CityModel mCityModel;
    private cityInfoAsyncTask mTask;


    class cityInfoAsyncTask extends AsyncTask<Void, Void, CityModel> {
        @Override
        protected CityModel doInBackground(Void... voids) {
            try {
                // TODO: не нравится что использую в этом классе объект mCityDataWeather
                mCityModel.getHttpWeather();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return mCityModel;
        }

        @Override
        protected void onPostExecute(CityModel cityModel) {
            super.onPostExecute(cityModel);
//            refreshData2View(cityModel);

        }
    }

    // обновление данных о погоде
    public void refreshDataWeather() throws ExecutionException, InterruptedException {
        Log.i(AnWeatherWidget.TAG_SERVICE,"start refreshDataWeather()");
        if (mTask != null) {
            mTask.cancel(true);
        }
        mTask = new cityInfoAsyncTask();
        try {
            Log.i(AnWeatherWidget.TAG_SERVICE,"mTask.execute()");
            mTask.execute();
            mCityModel = mTask.get(3, TimeUnit.SECONDS);
//            Log.i(AnWeatherWidget.TAG_SERVICE,"mTask.execute()");
        } catch (TimeoutException e) {
//            Toast.makeText(this, "Ошибка обновления данных", Toast.LENGTH_SHORT);
        }
//        refreshData2View(mCityDataWeather);
    }


    public GetWeatherCityService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(AnWeatherWidget.TAG_SERVICE,"Start service GetWeatherCityService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(AnWeatherWidget.TAG_SERVICE,"Start onStartCommand  GetWeatherCityService");
        mCityModel = new CityModel();
        mCityModel.setMYAPPID("76d6de6e46c704733f12c8738307dbb5");
        // получение данных через intent: имя города который нужно обновить
        mCityModel.setName(intent.getStringExtra(AnWeatherWidget.PARAM_CITY));

        //обновление погоды
        try {
            refreshDataWeather();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(AnWeatherWidget.TAG_SERVICE,"после refreshDataWeather() имя города: "+mCityModel.getName()+" -> "+mCityModel.getTemp());

        Log.i(AnWeatherWidget.TAG_SERVICE,"start refreshWidget");
        refreshWidget();
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(AnWeatherWidget.TAG_SERVICE,"Destroy GetWeatherCityService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // обновление виджета используя широковещательные сообщения
    private void refreshWidget(){
        Log.i(AnWeatherWidget.TAG_SERVICE,"refreshWidget имя города: "+mCityModel.getName()+" -> "+mCityModel.getTemp());
        // отправка виджету погоды данные о погоде
        Intent intent = new Intent(AnWeatherWidget.FORCE_WIDGET_UPDATE);
        intent.putExtra(AnWeatherWidget.PARAM_CITY,mCityModel.getName());
        intent.putExtra(AnWeatherWidget.PARAM_TEMP,Float.toString(mCityModel.getTemp()));
        intent.putExtra(AnWeatherWidget.PARAM_TIMEREFRESH,mCityModel.getTimeRefresh());
        intent.putExtra(AnWeatherWidget.PARAM_DESCWEATHER,mCityModel.getWeather("description"));
        intent.putExtra(AnWeatherWidget.PARAM_WEATHERIMAGE,mCityModel.getWeather("icon"));
        intent.putExtra(AnWeatherWidget.PARAM_WIND, Utils.windGradus2Rumb(mCityModel.getWinddirection())+" ("+Float.toString(mCityModel.getWindspeed())+" м/с)");



        //TODO: добавить передача остальных полей
        sendBroadcast(intent);
    }


    @Override
    public boolean stopService(Intent name) {
        Log.i(AnWeatherWidget.TAG_SERVICE,"Stop service GetWeatherCityService");
        return super.stopService(name);
    }
}
