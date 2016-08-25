package com.example.coderchang.onedaily.utils;

import android.util.Log;

/**
 * Created by coderchang on 16/8/25.
 */
public class DateUtils {

    public static String preDate(String currentDate) {
        String year = currentDate.substring(0, 4);
        int yearInt = Integer.parseInt(year);
        String month = currentDate.substring(4, 6);
        int monthInt = Integer.parseInt(month);
        String day = currentDate.substring(6, 8);
        int dayInt = Integer.parseInt(day);
        StringBuffer today = new StringBuffer();
        today.append(yearInt);
        today.append(monthInt);
        today.append(dayInt);
        Log.d("TAG", "today = " + today.toString());


        StringBuffer preDate = new StringBuffer();
        int maxMonthDay = 0;
        int FebMaxDay;

        if (yearInt % 4 == 0) {
            FebMaxDay = 29;
        } else {
            FebMaxDay = 28;
        }
        switch (monthInt){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                maxMonthDay = 31;
                break;
            case 2:
                maxMonthDay = FebMaxDay;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                maxMonthDay = 30;
                break;
        }
        if (monthInt == 1 && dayInt == 1) {
            preDate.append(--yearInt);
            preDate.append(12);
            preDate.append(maxMonthDay);

        } else if (dayInt == 1 && monthInt != 1) {
            preDate.append(yearInt);
            if (monthInt > 10) {
            } else {
                preDate.append(0);
            }
            preDate.append(--monthInt);
            preDate.append(maxMonthDay);
        } else {
            preDate.append(yearInt);
            if (monthInt > 10) {
            } else {
                preDate.append(0);
            }
            preDate.append(monthInt);
            if (dayInt > 10) {
            } else {
                preDate.append(0);
            }
            preDate.append(--dayInt);
        }
        Log.d("TAG", "preDate = " + preDate.toString());
        return preDate.toString();
    }
}
