package ru.kaefik.isaifutdinov.an_weather_widget;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
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
    ArrayList<String> mListView;
    Button btnSearch;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


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

//            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//            builder.setTitle("Какой город добавить?")
//                    .setCancelable(false)
//                    // добавляем одну кнопку для закрытия диалога
//                    .setNeutralButton("Назад",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog,
//                                                    int id) {
//                                    dialog.cancel();
//                                }
//                            })
//                    // добавляем переключатели
//                    .setSingleChoiceItems(ss, -1,
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog,
//                                                    int item) {
//                                    Log.i(TAG_SERVICE, " onPostExecute setSingleChoiceItems -> выбран элемент " + ss[item]);
//                                    Toast.makeText(
//                                            getApplicationContext(),
//                                            "Найденные города: "
//                                                    + ss[item],
//                                            Toast.LENGTH_SHORT).show();
//                                }
//                            });
//            builder.create();

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_city);

        mnameCityEditText = (EditText) findViewById(R.id.nameCityEditText);
        mresultSearchRecycleView = (RecyclerView) findViewById(R.id.resultSearchRecycleView);

        mListView = new ArrayList<String>();

        mRecyclerView = (RecyclerView) findViewById(R.id.resultSearchRecycleView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new CityModelRecyclerAdapter(mListView.toArray(new String[0]));
        mRecyclerView.setAdapter(mAdapter);

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
//                .toArray(new String[0]);

            }
        };

        btnSearch.setOnClickListener(oclBtnSearch);

    }

//    // Обработка кнопки поиска
//    public void onSearchClick(View view) throws InterruptedException, ExecutionException, TimeoutException {
//
//        Log.i(AnWeatherWidget.TAG_SERVICE, "start onSearchClickButton()");
//        if (mTask != null) {
//            mTask.cancel(true);
//        }
//        mTask = new cityInfoAsyncTask();
//
//        Log.i(AnWeatherWidget.TAG_SERVICE, "onSearchClickButton() -> mTask.execute()");
//        mTask.execute(mResult);
//        mListView = mTask.get(5, TimeUnit.SECONDS);
////                .toArray(new String[0]);
//
//    }

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
}
