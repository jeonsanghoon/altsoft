package com.altsoft.Framework;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.altsoft.Framework.module.BaseActivity;
import com.altsoft.loggalapp.R;
import com.bumptech.glide.Glide;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static com.kakao.util.helper.Utility.getPackageInfo;

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
        try {
            ProgressBar progress = activity.findViewById(R.id.progress);
            this.ProgressShow(activity, progress);
        }catch(Exception ex){}
    }
    /// 프로그래스바 보여주기
    public void ProgressShow(Activity activity, ProgressBar progress) {
        try {
            progress.setVisibility((int) 0);
        }catch(Exception ex){}
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


    private  String TAG = "Panoramio";

    private  int IO_BUFFER_SIZE = 4 * 1024;

    /**
     * Loads a bitmap from the specified url. This can take a while, so it should not
     * be called from the UI thread.
     *
     * @param url The location of the bitmap asset
     *
     * @return The bitmap, or null if it could not be loaded
     */

    public  Bitmap loadBitmap(String url) {
       return loadBitmap(url, -1,-1);
    }

    /**
     * Closes the specified stream.
     *
     * @param stream The stream to close.
     */
    private  void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                android.util.Log.e(TAG, "Could not close stream", e);
            }
        }
    }
    public  Bitmap loadBitmap(String url, int width, int height)
    {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;

        try {
            in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);

            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
            copy(in, out);
            out.flush();

            final byte[] data = dataStream.toByteArray();
            if(width == -1) {
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            }
            else{
                bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(data, 0, data.length), width,height,true);
            }


        } catch (IOException e) {
            Log.e(TAG, "Could not load Bitmap from: " + url);
        } finally {
            closeStream(in);
            closeStream(out);
        }

        return bitmap;
    }
    /**
     * Copy the content of the input stream into the output stream, using a
     * temporary byte array buffer whose size is defined by
     * {@link #IO_BUFFER_SIZE}.
     *
     * @param in The input stream to copy from.
     * @param out The output stream to copy to.
     * @throws IOException If any error occurs during the copy.
     */
    private  void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    /// App 해시 가져오기
    public  String getKeyHash(Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w("keyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }

    public String getComponentName(Activity activity)
    {
        PackageManager packageManager = activity.getPackageManager();
        String rtn = "";
        try {

            ActivityInfo info = packageManager.getActivityInfo(activity.getComponentName(), 0);
            Log.e("app", "Activity name:" + info.name);
            rtn = info.name;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return rtn;
    }
    public void AllActivityClose()
    {
        for(int i=0; i<BaseActivity.actList.size(); i++)
        {
            if(getComponentName(BaseActivity.actList.get(i)) != "mainactivity")
            {
                BaseActivity.actList.get(i).finish();
            }
        }
    }
    public void ActivityClose(Activity activity)
    {
        activity.finish();
    }
}
