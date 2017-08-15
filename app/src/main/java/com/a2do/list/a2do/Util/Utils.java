package com.a2do.list.a2do.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.a2do.list.a2do.database.DatabaseHelper.PROP_DUE_DATE;

/**
 * Created by Nitin on 8/15/2017.
 */

public class Utils {

    public static Date getDate(String dateString) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        try {
            calendar.setTime( sdf.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  calendar.getTime();
    }
}
