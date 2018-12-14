package com.altsoft.Framework;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.altsoft.loggalapp.R;
import com.bumptech.glide.Glide;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class Common {

    /// 프로그래스바 숨기기
    public void ProgressHide(Activity activity) {
        ProgressBar progress = activity.findViewById(R.id.progress);
        this.ProgressHide(activity,progress);
    }
    /// 프로그래스바 숨기기
    public void ProgressHide(Activity activity, ProgressBar progress) {
        progress.setVisibility((int) 4);
    }

    /// 프로그래스바 보여주기
    public void ProgressShow(Activity activity) {
        ProgressBar progress=  activity.findViewById(R.id.progress);
        this.ProgressShow(activity, progress);
    }
    /// 프로그래스바 보여주기
    public void ProgressShow(Activity activity, ProgressBar progress) {
        progress.setVisibility((int)0);
    }

    /// 날짜형 포맷 여부
    public  boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim() + " 00:00:00");
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
    /// 시간형 포맷 여부
    public boolean isValidTime(String inTime) {
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
    // 현재 일자정보 가져오기
    public  String getCurrentTimeString()
    {
        return getCurrentTimeString("yyyyMMddHHmmss");
    }
    public String getCurrentTimeString(String Format) {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String getTime = sdf.format(date);

        return getTime;
    }

    public String leftPad(String originalString, int length,
                                 char padCharacter) {
        String paddedString = originalString;
        while (paddedString.length() < length) {
            paddedString = padCharacter + paddedString;
        }
        return paddedString;
    }
    public String getCurrentTime(String timeFormat){
        return new SimpleDateFormat(timeFormat).format(System.currentTimeMillis());
    }

    public  Integer dpToPx(Context ctx, Integer dp) {
        float density = ctx.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    /// 키보드 보이기/ 숨기기
    public boolean hideSoftInputWindow(Context context, View edit_view, boolean bState) {

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if ( bState )
            return imm.showSoftInput(edit_view, 0);
        else
            return imm.hideSoftInputFromWindow
                    (edit_view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    public  void getTotalHeightofListView(ListView listView) {

        ListAdapter mAdapter = listView.getAdapter();

        int totalHeight = 0;

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);

            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),

                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            totalHeight += mView.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (mAdapter.getCount() - 1));


        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public Bitmap getBitmapFromURL(String src) throws ExecutionException, InterruptedException {
        Bitmap bitmap = null;
        InputStream stream = null;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;

        try {
            stream = getHttpConnection(src);
            bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
            stream.close();
        }
        catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("downloadImage"+ e1.toString());
        }
        return bitmap;
    }

    private  InputStream getHttpConnection(String urlString)  throws IOException {

        InputStream stream = null;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        try {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("downloadImage" + ex.toString());
        }
        return stream;
    }


}
