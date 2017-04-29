package com.example.harta1.spendingtracker.DataBase;

import android.provider.BaseColumns;
import android.provider.ContactsContract;

/**
 * Created by harta1 on 4/27/2017.
 */

public class DataBaseContract {
    private DataBaseContract(){}

    public static final class MoneyEntry implements BaseColumns {
        public static final String TABLE_NAME = "money_entries";

        public static final String ENTRY_ID = "_id";

        public static final String ENTRY_AMOUNT = "entry_amount";

        public static final String Date = "date";

        public static final String Purchase_Type = "type";


    }

}
