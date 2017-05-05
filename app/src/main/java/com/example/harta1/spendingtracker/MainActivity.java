package com.example.harta1.spendingtracker;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.harta1.spendingtracker.GraphData.GraphDataActivity;
import com.example.harta1.spendingtracker.MoneyTracker.MoneyTrackerFragment;
import com.example.harta1.spendingtracker.Utilities.MoneyFormat;

import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {
    private MoneyTrackerFragment moneyTrackerFragment;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private EditText budgetAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        budgetAmount = (EditText) findViewById(R.id.budget_input);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = fragmentManager.getFragments().get(0);

        moneyTrackerFragment = (MoneyTrackerFragment) fragment;



        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close ){
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);



                SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                String amount = MoneyFormat.correctMoneyFormat(budgetAmount.getText().toString());
                if(!amount.isEmpty()) {
                    editor.putString(getString(R.string.budget_amount_key), amount);
                }
                editor.putInt(getString(R.string.budget_type_key), translateBudgetType(view));
                editor.commit();

                moneyTrackerFragment.setBudget();

            }

            public void onDrawerOpened(View view){
                super.onDrawerOpened(view);

                SharedPreferences sharedPrefernces= getPreferences(Context.MODE_PRIVATE);
                String budgetAmountValue = sharedPrefernces.getString(getString(R.string.budget_amount_key), getString(R.string.budget_amount_default_value));
                budgetAmount.setText(budgetAmountValue);
            }
        };


        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionBarDrawerToggle.syncState();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }


    public int translateBudgetType(View view){
        int radiobutton = ((RadioGroup) view.findViewById(R.id.RB_budget_type)).getCheckedRadioButtonId();

        switch (radiobutton){
            case R.id.RB_DAY:
                return MoneyTrackerFragment.DAY;
            case R.id.RB_WEEK:
                return  MoneyTrackerFragment.WEEK;
            case R.id.RB_MONTH:
                return  MoneyTrackerFragment.MONTH;
        }
        return 3;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);

                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.MI_delete:
                moneyTrackerFragment.deleteItems();
                break;
            case R.id.MI_graph:
                Intent intent = new Intent(this, GraphDataActivity.class);
                startActivity(intent);


        }
        return true;
    }

    public interface budgetCB{
        public void setBudget();

        void deleteItems();
    }

    public interface deleteItems{
        void changeSetting();


    }

}
