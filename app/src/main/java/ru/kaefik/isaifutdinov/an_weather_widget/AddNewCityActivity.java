package ru.kaefik.isaifutdinov.an_weather_widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ru.kaefik.isaifutdinov.an_weather_widget.adapter.CityModelRecyclerAdapter;
import ru.kaefik.isaifutdinov.an_weather_widget.utils.Utils;

public class AddNewCityActivity extends AppCompatActivity {

    EditText mnameCityEditText;
    RecyclerView mresultSearchRecycleView;
    String mResult;
    cityInfoAsyncTask mTask;
    List<String> mListView;
    Button btnSearch;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private int mAppWidgetId; // ID текущего виджета

    public static String TAG_SERVICE = "AnWeatherWidget";
    public static final String WIDGET_PREF = "anweatherwidgetconfig";
    private SharedPreferences mSPref;//файл настроек


    // принимает входной параметр - назв-е города который нужно искать, выходной параметр это то что было выбрано из предоставленных вариантов
    class cityInfoAsyncTask extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... voids) {
            ArrayList<String> rr = new ArrayList<String>();

            // TODO: не нравится что использую в этом классе объект mCityDataWeather
            Log.i(ConfigActivity.TAG_SERVICE, " AddNewCityActivity: onPostExecute -> voids[0] " + voids[0]);
            rr = Utils.getLikeNameCity(voids[0]);

            return rr;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
//            super.onPostExecute(result);

            final String[] ss = result.toArray(new String[0]);
            Log.i(ConfigActivity.TAG_SERVICE, " AddNewCityActivity: onPostExecute -> ss " + ss.toString());
            mListView = result;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_city);

        setResult(RESULT_CANCELED);


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


        mnameCityEditText = (EditText) findViewById(R.id.nameCityEditText);
        mresultSearchRecycleView = (RecyclerView) findViewById(R.id.resultSearchRecycleView);


        mRecyclerView = (RecyclerView) findViewById(R.id.resultSearchRecycleView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(false);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        Log.i(ConfigActivity.TAG_SERVICE, " AddNewCityActivity: onCreate -> ");
        mListView = new ArrayList<String>();
        Log.i(ConfigActivity.TAG_SERVICE, " AddNewCityActivity: onCreate  mListView -> " + mListView.toString());

        String[] stringArray = mListView.toArray(new String[0]);
        Log.i(ConfigActivity.TAG_SERVICE, " AddNewCityActivity: onCreate  stringArray -> " + stringArray.toString());


        mAdapter = new CityModelRecyclerAdapter(mListView, new CityModelRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final String item) {
                Log.i(ConfigActivity.TAG_SERVICE, " выбран элемент  -> " + item);


                AlertDialog.Builder builder = new AlertDialog.Builder(AddNewCityActivity.this);
                builder.setTitle("Сделали правильный выбор?");
                builder.setMessage(item);
                builder.setCancelable(true);
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() { // Кнопка ОК
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        mRecyclerView.setAdapter(mAdapter);
        Log.i(TAG_SERVICE, " AddNewCityActivity: onCreate  stringArray -> " + stringArray.toString());


        btnSearch = (Button) findViewById(R.id.searchButton);
        View.OnClickListener oclBtnSearch = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i(AnWeatherWidget.TAG_SERVICE, "start onSearchClickButton()");
                if (mTask != null) {
                    mTask.cancel(true);
                }
                mTask = new cityInfoAsyncTask();
                mResult = mnameCityEditText.getText().toString();

                Log.i(AnWeatherWidget.TAG_SERVICE, "onSearchClickButton() -> mTask.execute()");
                mTask.execute(mResult);
                try {
                    mListView = mTask.get(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                Log.i(AnWeatherWidget.TAG_SERVICE, "onSearchClickButton() -> mListView " + mListView.toString());

                mRecyclerView.setAdapter(new CityModelRecyclerAdapter(mListView, new CityModelRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(final String item) {
                        Log.i(ConfigActivity.TAG_SERVICE, " выбран элемент  -> " + item);

                        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewCityActivity.this);
                        builder.setTitle("Сделали правильный выбор?");
                        builder.setMessage(item);
                        builder.setCancelable(true);
                        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() { // Кнопка ОК
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i(ConfigActivity.TAG_SERVICE, " AlertDialog  выбран элемент  -> " + item);
                                setResult(item);
                                dialog.dismiss(); // Отпускает диалоговое окно

                                saveStringParametersToCfg(context, String.valueOf(mAppWidgetId), getResult());

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
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }));

            }
        };

        btnSearch.setOnClickListener(oclBtnSearch);

    }


    @Override
    protected void onStart() {
        super.onStart();
//        Log.i(TAG_SERVICE, "onStart  ConfigActivity");
//        //-----------------------
//        mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
//        Intent intent = getIntent();
//        Bundle extras = intent.getExtras();
//        if (extras != null) {
//            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
//                    AppWidgetManager.INVALID_APPWIDGET_ID);
//        }

//        final Context context = this;

        //-----------------------

//        // Обработка события на клик по элементу списка
//        mNameCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String cityNameString = adapter.getCityModel(position);
//
//                Log.i(TAG_SERVICE, " OnItemClick  ConfigActivity -> выбран город " + cityNameString + "  id виджета: " + String.valueOf(mAppWidgetId));
//
//                saveStringParametersToCfg(context, String.valueOf(mAppWidgetId), cityNameString);
//
//                Intent resulValue = new Intent(AnWeatherWidget.CLICK_WIDGET_BUTTON);
//                resulValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
//                //обновление виджета после отработки ConfigActivity
//                try {
//                    AnWeatherWidget.updateAppWidget(context, AppWidgetManager.getInstance(context), mAppWidgetId);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                setResult(RESULT_OK, resulValue);
//                finish();
//            }
//        });
    }

    // возврат к основной активити MainActivity
    public void goBackConfigActivity() {
//        if (mTask != null) {
//            mTask.cancel(true);
//        }
        Intent intent = new Intent(this, ConfigActivity.class);
        intent.putExtra("name", getResult());
        setResult(RESULT_OK, intent);
        finish();
    }

    public void setResult(String result) {
        mResult = result;
    }

    public String getResult() {
        return mResult;
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

}

