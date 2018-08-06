package com.altsoft.loggalapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.altsoft.Framework.Global;

import java.util.Calendar;
import java.util.TimeZone;

public class SignageControlActivity extends AppCompatActivity {

    private EditText txtDate;private EditText txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signage_control);
        txtDate =(EditText)findViewById(R.id.txtDate);
        txtTime =(EditText)findViewById(R.id.txtTime);
        txtDate.setFocusable(false);
        txtTime.setFocusable(false);
    }
    public void btnCarema_onClick(View v){
        Toast.makeText(this,"카메라가 클릭되었습니다.", Toast.LENGTH_LONG).show();
    }
    public void btnVideo_onClick(View v){
        Toast.makeText(this,"비디오가 클릭되었습니다.", Toast.LENGTH_LONG).show();
    }

    public void btnGallery_onClick(View v){
        Toast.makeText(this,"갤러리가 클릭되었습니다.", Toast.LENGTH_LONG).show();
    }

    public void txtDate_onClick(View v){
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());




        final Calendar c = Calendar.getInstance();
        try {
            if (Global.getCommon().isValidDate(txtDate.getText().toString()) == true) {
                String sDate = txtDate.getText().toString();
                String[] arrDate = sDate.split("\\.");
                mYear = Integer.parseInt(arrDate[0]);
                mMonth =Integer.parseInt( arrDate[1])-1;
                mDay = Integer.parseInt(arrDate[2]);
            }
            else{
                throw new Exception("날짜포맷이 아닙니다.");
            }
        }catch(Exception ex) {
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String month = Global.getCommon().leftPad(Integer.toString(monthOfYear + 1), 2, '0');
                        String day = Global.getCommon().leftPad(Integer.toString(dayOfMonth + 1), 2, '0');

                        txtDate.setText(year + "." + month + "." + day);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void txtTime_onClick(View view) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        try {
            if (Global.getCommon().isValidTime(txtTime.getText().toString()) == true)  {

                String sTime = txtTime.getText().toString();
                String[] arrTime = sTime.split(":");

                mHour = Integer.parseInt(arrTime[0]);
                mMinute =Integer.parseInt(arrTime[1]);


            }
            else {
                throw new Exception("시간포맷이 아닙니다.");
            }
        }catch(Exception ex) {
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
        }


        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String hour = Global.getCommon().leftPad(Integer.toString(hourOfDay), 2, '0');
                        String min = Global.getCommon().leftPad(Integer.toString(minute), 2, '0');
                        txtTime.setText(hour + ":" + min);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
}
