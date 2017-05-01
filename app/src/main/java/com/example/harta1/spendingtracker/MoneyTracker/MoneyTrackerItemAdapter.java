package com.example.harta1.spendingtracker.MoneyTracker;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.harta1.spendingtracker.DataBase.DataBaseContract;
import com.example.harta1.spendingtracker.MainActivity;
import com.example.harta1.spendingtracker.R;

/**
 * Created by harta1 on 4/26/2017.
 */

public class MoneyTrackerItemAdapter extends RecyclerView.Adapter<MoneyTrackerItemAdapter.ViewHolder> {
     private static Cursor cursor;

    private boolean delete = false;

    MoneyTrackerFragment moneyTrackerFragment;

    public MoneyTrackerItemAdapter(Cursor cursor, Fragment fragment){
        this.cursor = cursor;
        moneyTrackerFragment = (MoneyTrackerFragment) fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.money_list_item,
        parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToNext();

        int amount_column = cursor.getColumnIndex(DataBaseContract.MoneyEntry.ENTRY_AMOUNT);
        int category_column = cursor.getColumnIndex(DataBaseContract.MoneyEntry.Purchase_Type);
        int id_column = cursor.getColumnIndex(DataBaseContract.MoneyEntry._ID);



        if(delete){
            holder.delete_item.setVisibility(View.VISIBLE);
        } else {
            holder.delete_item.setVisibility(View.GONE);
        }




        try {
            holder.money_value.setText(cursor.getString(amount_column));
            holder.money_category.setText(cursor.getString(category_column));
            holder.id = cursor.getInt(id_column);
        } catch (IndexOutOfBoundsException e) {
            Log.d("onBindViewHolder", e.toString());

        } catch (Exception e){
            Log.d("onBindViewHolder", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        if(cursor != null){
            return cursor.getCount();
        }
        return 0;

    }

    public void swapCursor(Cursor cursor){
            this.cursor = cursor;


    }


    public void changeSetting() {
        if(delete){
            delete = false;
        } else {
            delete = true;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView money_value;
        TextView money_category;
        ImageButton delete_item;
        int id;

        public ViewHolder(View itemView) {
            super(itemView);
            delete_item = (ImageButton) itemView.findViewById(R.id.IB_delete);
            money_value = (TextView) itemView.findViewById(R.id.TV_money_item_value);
            money_category = (TextView) itemView.findViewById(R.id.TV_moeny_item_category);

            delete_item.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            moneyTrackerFragment.deleteItem(id);

        }


    }
    public interface viewHolderCB{
        void deleteItem(int id);
    }

}
