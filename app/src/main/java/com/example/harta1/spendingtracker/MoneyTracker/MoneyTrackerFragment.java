package com.example.harta1.spendingtracker.MoneyTracker;

import android.support.v4.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harta1.spendingtracker.DataBase.DataBaseContract;
import com.example.harta1.spendingtracker.DataBase.MoneyEntriesDbHelper;
import com.example.harta1.spendingtracker.MainActivity;
import com.example.harta1.spendingtracker.R;
import com.example.harta1.spendingtracker.Utilities.MoneyFormat;

/**
 * Created by harta1 on 4/26/2017.
 */

public class MoneyTrackerFragment extends Fragment implements MainActivity.budgetCB{
    private SQLiteDatabase sqLiteDatabase;

    MoneyEntriesDbHelper moneyEntriesDbHelper;

    private ImageView budgetIndicator;

    private RadioGroup select_category;

    private TextView total_spent;

    private EditText add_amount_value;

    private Button add_amount;

    private static Cursor cursor;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MoneyTrackerItemAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_money_tracker,container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);




        setUpViews(v);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        moneyEntriesDbHelper = new MoneyEntriesDbHelper(getActivity());

        sqLiteDatabase = moneyEntriesDbHelper.getWritableDatabase();

        cursor = prepareDatabase();

        adapter = new MoneyTrackerItemAdapter(cursor);
        recyclerView.setAdapter(adapter);

        updateValue();
    }

    public void setUpViews(View r){
        budgetIndicator = (ImageView) r.findViewById(R.id.budget_indicator);

        select_category = (RadioGroup) r.findViewById(R.id.RG_select_category);

        total_spent = (TextView) r.findViewById(R.id.TV_budget_left_amount);

        add_amount = (Button) r.findViewById(R.id.B_addAmount);

        add_amount_value = (EditText) r.findViewById(R.id.ET_addAmount);

        add_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String moneyAmount = add_amount_value.getText().toString();

                String foramttedAmount = MoneyFormat.correctMoneyFormat(moneyAmount);

               if(!foramttedAmount.isEmpty()){

                   String category = ((RadioButton) v.getRootView().findViewById(select_category.getCheckedRadioButtonId())).getText().toString();

                   insertData(foramttedAmount, category);

                   add_amount_value.setText("");

                   updateValue();

               }
            }
        });

    }

    public void insertData(String amount, String category){

        ContentValues values = new ContentValues();

        values.put(DataBaseContract.MoneyEntry.ENTRY_AMOUNT, amount);
        values.put(DataBaseContract.MoneyEntry.Purchase_Type, category);

        sqLiteDatabase.insert(DataBaseContract.MoneyEntry.TABLE_NAME,null, values);

        adapter.swapCursor(sqLiteDatabase.query(DataBaseContract.MoneyEntry.TABLE_NAME, null, null, null, null, null,null));
        adapter.notifyDataSetChanged();


    }

    public Cursor prepareDatabase() {

        Cursor cursor = sqLiteDatabase.query(DataBaseContract.MoneyEntry.TABLE_NAME, null, null, null, null, null,null);


        return cursor;

    }


    @Override
    public void onPause() {
        super.onPause();
        sqLiteDatabase.close();
        moneyEntriesDbHelper.close();
    }

    /*
        *This updates the total spent value and check to see if the user has gone over budget.
         */
    public void updateValue(){
        SharedPreferences sharedPrefernces= getActivity().getPreferences(Context.MODE_PRIVATE);
        String budgetAmountValue = sharedPrefernces.getString(getString(R.string.budget_amount_key), getString(R.string.budget_amount_default_value));


        double budgetDifference =  Double.valueOf(budgetAmountValue) - moneyEntriesDbHelper.getCurrentToalSpent();

        budgetDifference *= 100;
        budgetDifference = Math.round(budgetDifference);
        budgetDifference /=100;

        if(budgetDifference < 0){
            budgetIndicator.setImageResource(R.color.overBudget);
        } else {
            budgetIndicator.setImageResource(R.color.underBudget);
        }
        total_spent.setText(String.valueOf(budgetDifference));

    }

    @Override
    public void setBudget() {
        updateValue();
    }

    @Override
    public void deleteItems() {


        adapter.changeSetting();
        adapter.notifyDataSetChanged();

        adapter.swapCursor(sqLiteDatabase.query(DataBaseContract.MoneyEntry.TABLE_NAME, null, null, null, null, null,null));
        adapter.notifyDataSetChanged();
    }
}
