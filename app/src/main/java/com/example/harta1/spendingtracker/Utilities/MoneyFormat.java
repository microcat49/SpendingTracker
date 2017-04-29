package com.example.harta1.spendingtracker.Utilities;

import java.util.regex.Pattern;

/**
 * Created by harta1 on 4/27/2017.
 */

public class MoneyFormat {
    /**
     * This will take .d, d, .dd and turn them into *d.dd value
     * @param value Input string poorly formatted string value.
     * @return Will return a *d.dd value. If the value is incorrect it will return an empty string.
     */
    public static String correctMoneyFormat(String value){
        Pattern p = Pattern.compile("\\d+\\.\\d\\d");

        if(p.matcher(value).matches())
        {
            return value;
        } else {
            Pattern p2 = Pattern.compile("\\.\\d\\d");
            if(p2.matcher(value).matches()){
                return "0" + value;
            }

            Pattern p3 = Pattern.compile("\\d+");

            if(p3.matcher(value).matches()){
                return value + ".00";
            }

            Pattern p4 = Pattern.compile("\\d+\\.");
            if(p4.matcher(value).matches()){
                return value + "00";
            }

        }

        return "";

    }
}
