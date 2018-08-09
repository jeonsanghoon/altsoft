package com.altsoft.asynctask;

import android.os.AsyncTask;

import com.altsoft.Framework.FtpInfo;

/**
 * AsyncTask<Integer, Integer, Boolean>은 아래와 같은 형식이다
 * AsyncTask<전달받은값의종류, Update값의종류, 결과값의종류>
 * ex) AsyncTask<Void, Integer, Void>
 */
public class FtpUploadAsyncTask extends AsyncTask<String, String, Void> {

    @Override
    protected Void doInBackground(String... strings) {
        String host = strings[0];
        String userId = strings[1];
        String pw    = strings[2];

        try {
            FtpInfo ftp = new FtpInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}



