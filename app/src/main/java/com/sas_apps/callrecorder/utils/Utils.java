package com.sas_apps.callrecorder.utils;
/*
 * Created by Shashank Shinde.
 */

import android.app.ActivityManager;
import android.content.Context;
import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Utils {


    public static final String FOLDER_NAME = "CALL_REC";
    public static final String FILE_NAME = "REC";

    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        return DateFormat.format("dd-MM-yy  hh:mm aaa", cal).toString();
    }


    public static String getDuration(String duration){
        long temp=Long.parseLong(duration);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(temp);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(temp);
        return String.valueOf(minutes)+":"+String.valueOf(seconds);
    }


}
