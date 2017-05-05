package com.example.harta1.spendingtracker.GraphData;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.harta1.spendingtracker.DataBase.DataBaseContract;
import com.example.harta1.spendingtracker.DataBase.MoneyEntriesDbHelper;
import com.example.harta1.spendingtracker.R;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.charts.StackedBarChart;
import org.eazegraph.lib.models.PieModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by harta1 on 5/4/2017.
 */

public class GraphDataActivity extends AppCompatActivity {

    static PieChart pieChart;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphdata);



    }

    @Override
    protected void onStart() {
        super.onStart();

        pieChart = (PieChart) findViewById(R.id.stackedbarchart);
        new FetchData().execute("");


    }

    class FetchData extends AsyncTask<String, Void, Map<String, Double>> {
        int num = 0;
        @Override
        protected Map<String, Double> doInBackground(String... params) {
            MoneyEntriesDbHelper moneyEntriesDbHelper = new MoneyEntriesDbHelper(getBaseContext());
            SQLiteDatabase sqLiteDatabase = moneyEntriesDbHelper.getReadableDatabase();

            Cursor cusor = sqLiteDatabase.query(DataBaseContract.MoneyEntry.TABLE_NAME, new String[]{DataBaseContract.MoneyEntry.ENTRY_AMOUNT, DataBaseContract.MoneyEntry.Purchase_Type},
                    null, null, null, null, null);

            Map<String, Double> map = new HashMap<String, Double>();

            int entry_col = cusor.getColumnIndex(DataBaseContract.MoneyEntry.ENTRY_AMOUNT);
            int type_col = cusor.getColumnIndex(DataBaseContract.MoneyEntry.Purchase_Type);

            String type;
            double amount;

            while(cusor.moveToNext()){

                type = cusor.getString(type_col);

                if(map.containsKey(type)){
                    amount = map.get(cusor.getString(type_col)) + 1;
                    map.put(type, amount);
                } else {
                    map.put(type, Double.valueOf(cusor.getString(entry_col)));
                }


            }

            return map;

        }

        @Override
        protected void onPostExecute(Map<String, Double> stringDoubleMap) {
            super.onPostExecute(stringDoubleMap);

            Set<String> g = stringDoubleMap.keySet();


            for(String a : g){
                pieChart.addPieSlice(new PieModel(a,  stringDoubleMap.get(a).floatValue(), getColor()));
                pieChart.startAnimation();
            }


        }

        public int getColor(){

            switch (num) {
                case 0:
                    num +=1;
                    return Color.parseColor("#FE6DA8");


                case 1:
                    num +=1;
                    return Color.parseColor("#56B7F1");


                case 2:
                    num +=1;
                    return Color.parseColor("#CDA67F");


                case 3:
                    num =0;
                    return Color.parseColor("#FED70E");


                default:
                    return 0;
            }
        }
    }


}
