package com.altsoft.asynctask;

import android.app.Activity;
import android.os.AsyncTask;

import com.altsoft.Framework.Global;

/**
 * AsyncTask<Integer, Integer, Boolean>은 아래와 같은 형식이다
 * AsyncTask<전달받은값의종류, Update값의종류, 결과값의종류>
 * ex) AsyncTask<Void, Integer, Void>
 */
public class FtpUploadAsyncTask extends AsyncTask<String, String, String> {
    static  Activity _activity;
    public FtpUploadAsyncTask(Activity activity)
    {
        _activity = activity;

    }
    @Override
    protected String doInBackground(String... strings) {
        String host = strings[0];
        String rtn = "";
        try {


            String FilePath = strings[0];
            String[] arrFile = Global.getFileInfo().getFileAndExtension(FilePath);
            String destFileName = Global.getCommon().getCurrentTimeString() + "." + arrFile[1];
            String strDelectory = Global.getCommon().getCurrentTimeString("yyyyMM");
            Global.getFtpInfo().ftpUploadFile(FilePath,"", strDelectory);

            rtn = Global.getFtpInfo().ftpGetDirectory() + "/" + strDelectory + "/" + destFileName;

            Global.getCommon().ProgressHide(_activity);

        } catch (Exception e) {
            e.printStackTrace();
        }
        rtn = (rtn == null) ? "" : rtn;
        return rtn;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}



