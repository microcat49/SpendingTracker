package com.example.harta1.spendingtracker;

import com.example.harta1.spendingtracker.Utilities.MoneyFormat;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);


        assertNotEquals(MoneyFormat.correctMoneyFormat("2"),"");
        assertNotEquals(MoneyFormat.correctMoneyFormat(".00"),"");
        assertNotEquals(MoneyFormat.correctMoneyFormat("0"),"");
        assertNotEquals(MoneyFormat.correctMoneyFormat(".3"),"");
    }
}