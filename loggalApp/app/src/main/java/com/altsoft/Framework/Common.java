package com.altsoft.Framework;

import android.app.Activity;
import android.widget.ProgressBar;

import com.altsoft.loggalapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Common {
    public void ProgressHide(Activity activity) {
       ProgressBar progress=  activity.findViewById(R.id.progress);
        progress.setVisibility((int)4);


    }
    public void ProgressShow(Activity activity) {
        ProgressBar progress=  activity.findViewById(R.id.progress);
        progress.setVisibility((int)0);
    }
    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim() + " 00:00:00");
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
    public static boolean isValidTime(String inTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        dateFormat.setLenient(false);
        try {
            inTime = inTime.trim();
            if(inTime.trim().length() == 5) {inTime = inTime + ":00";}
            dateFormat.parse("2000.01.01 " + inTime.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public static String leftPad(String originalString, int length,
                                 char padCharacter) {
        String paddedString = originalString;
        while (paddedString.length() < length) {
            paddedString = padCharacter + paddedString;
        }
        return paddedString;
    }
    public static String getCurrentTime(String timeFormat){
        return new SimpleDateFormat(timeFormat).format(System.currentTimeMillis());
    }
}
