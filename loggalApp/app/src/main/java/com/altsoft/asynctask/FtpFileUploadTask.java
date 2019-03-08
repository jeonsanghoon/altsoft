package com.altsoft.asynctask;

import android.app.Activity;
import android.os.AsyncTask;

import com.altsoft.Framework.Global;
import com.altsoft.Interface.AsyncCallbackOnEventListener;

/**
 * AsyncTask<Integer, Integer, Boolean>은 아래와 같은 형식이다
 * AsyncTask<전달받은값의종류, Update값의종류, 결과값의종류>
 * ex) AsyncTask<Void, Integer, Void>
 */
public class FtpFileUploadTask extends AsyncTask<String, Integer, String> {
    private AsyncCallbackOnEventListener<String> mCallBack;
    public Boolean bSuccess = true;
    public Exception mException;

    public FtpFileUploadTask(AsyncCallbackOnEventListener callback) {

        mCallBack = callback;

    }
    @Override
    protected String doInBackground(String... strings) {
        Global.getCommon().ProgressShow();
        String uploadFileName = strings[1];
        try {

            Global.getFtpInfo().ftpUploadFile(strings[0],strings[1],strings[2]);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return uploadFileName;
    }

    @Override
    protected void onProgressUpdate(Integer... values) { // 파일 다운로드 퍼센티지 표시 작업
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        // doInBackground 에서 받아온 total 값 사용 장소
        super.onPostExecute(result);
        Global.getCommon().ProgressHide();
        if (mCallBack != null) {
            if(true)
                mCallBack.onSuccess(result);
            else
                mCallBack.onFailure(mException);
        }
    }
}



