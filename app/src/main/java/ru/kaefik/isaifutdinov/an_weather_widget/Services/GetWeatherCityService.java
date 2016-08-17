package ru.kaefik.isaifutdinov.an_weather_widget.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ru.kaefik.isaifutdinov.an_weather_widget.city.CityModel;

public class GetWeatherCityService extends Service {

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
        if (mTask != null) {
            mTask.cancel(true);
        }
        mTask = new cityInfoAsyncTask();
        try {
            mTask.execute();
            mCityModel = mTask.get(3, TimeUnit.SECONDS);
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
        mCityModel = new CityModel("Kazan");
        mCityModel.setMYAPPID("76d6de6e46c704733f12c8738307dbb5");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        refreshDataWeather();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }




}
