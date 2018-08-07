package com.altsoft.loggalapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.VideoView;

import com.altsoft.Framework.Global;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.Calendar;
import java.util.TimeZone;

public class SignageControlActivity extends AppCompatActivity {

    private EditText txtDate;private EditText txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private static final int IMAGE_CAPTURE = 1;
    private static final int IMAGE_LOAD =2;
    private static final int VIDEO_CAPTURE =3;
    ImageView imageView;
    VideoView videoView;
    Uri videoUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signage_control);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView) findViewById(R.id.imgPreView);
        videoView = (VideoView) findViewById(R.id.videoPreView);

        setTitle("사이니지제어");
        txtDate =(EditText)findViewById(R.id.txtDate);
        txtTime =(EditText)findViewById(R.id.txtTime);
        txtDate.setFocusable(false);
        txtTime.setFocusable(false);

    }
    public void btnCarema_onClick(View v){
        Toast.makeText(this,"카메라가 클릭되었습니다.", Toast.LENGTH_LONG).show();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, IMAGE_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView imageView = (ImageView) findViewById(R.id.imgPreView);
        VideoView videoView = (VideoView) findViewById(R.id.videoPreView);
        Bitmap bm;
        isImgPreViewShow(true);
        if (requestCode == IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bm = (Bitmap) extras.get("data");
            imageView.setImageBitmap(bm);
        }
        else if (requestCode == IMAGE_LOAD && resultCode == RESULT_OK && null != data) {


            // Let's read picked image data - its URI
            Uri imageUri = data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .apply(new RequestOptions().override(100, 100))
                    .into(imageView)
            ;


      }
      else if (requestCode ==  VIDEO_CAPTURE && resultCode == RESULT_OK) {

            videoView.setVideoURI(videoUri);

            videoView.setMediaController(new MediaController(this));
            videoView.requestFocus();
            videoView.setZOrderOnTop(true);
            videoView.start();

            //isImgPreViewShow(false);
        }
    }

    private void isImgPreViewShow(Boolean isShow) {
        /*
        LinearLayout layVideo = (LinearLayout)findViewById(R.id.layVideo);
        LinearLayout layImage = (LinearLayout)findViewById(R.id.layImage);
        if(isShow == true)
        {
            imageView.setVisibility(layImage.VISIBLE);
            videoView.setVisibility(layVideo.INVISIBLE);
        }
        else{
            imageView.setVisibility(layImage.INVISIBLE);
            videoView.setVisibility(layVideo.VISIBLE);
        }*/
    }




    public void btnVideo_onClick(View v){
        Toast.makeText(this,"비디오가 클릭되었습니다.", Toast.LENGTH_LONG).show();
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            File mediaFile = new File(
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/myvideo.mp4");
            videoUri = Uri.fromFile(mediaFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
            startActivityForResult(intent, VIDEO_CAPTURE);
        } else {
            Toast.makeText(this, "No camera on device", Toast.LENGTH_LONG).show();
        }
    }

    public void btnGallery_onClick(View v){
        Toast.makeText(this,"갤러리가 클릭되었습니다.", Toast.LENGTH_LONG).show();
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, IMAGE_LOAD);


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
