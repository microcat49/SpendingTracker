package com.example.harta1.spendingtracker.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by harta1 on 4/27/2017.
 */

public class MoneyEntriesDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "money_entries.db";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "+
            DataBaseContract.MoneyEntry.TABLE_NAME + " (" +
    DataBaseContract.MoneyEntry.ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DataBaseContract.MoneyEntry.ENTRY_AMOUNT + " TEXT," +
            DataBaseContract.MoneyEntry.Purchase_Type + " TEXT," +
            DataBaseContract.MoneyEntry.Date + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +"); ";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DataBaseContract.MoneyEntry.TABLE_NAME;


    public MoneyEntriesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);

    }

    /**
     * Reutrn the total spent in a week.
     */
    public Double getCurrentToalSpent(){
        Double total = 0.0;

        Cursor cursor = getReadableDatabase().query(DataBaseContract.MoneyEntry.TABLE_NAME, new String[]{DataBaseContract.MoneyEntry.ENTRY_AMOUNT} , null, null, null, null, null);
        try {
            while (cursor.moveToNext()) {
                total += Double.valueOf(cursor.getString(0));

            }
        }finally{
            cursor.close();
        }

        return total;
    }
}
