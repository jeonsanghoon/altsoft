package com.altsoft.Framework.module;


import android.annotation.SuppressLint;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.altsoft.Framework.DataInfo.EMail;
import com.altsoft.Framework.Global;
import com.altsoft.Interface.AsyncCallbackOnEventListener;
import com.altsoft.loggalapp.FindPasswordActivity;
import com.altsoft.loggalapp.R;
import com.altsoft.model.MAIL_INFO;

import java.util.ArrayList;
import java.util.Arrays;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;


@SuppressLint("Registered")
public abstract class BaseActivity extends AppCompatActivity {

    public static ArrayList<Activity> actList = new ArrayList<Activity>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Global.setCurrentActivity(this);
    }
    @Override
    protected  void onStart()
    {
        super.onStart();
        Global.setCurrentActivity(this);
        if(Global.getCommon().getComponentName(this).toLowerCase() != "mainactivity") {
            actList.add(this);
        }
    }

    //region Methods Toolbar
    public void onInitToolbar(Toolbar toolBar) {
        onInitToolbar(toolBar, getString(R.string.clear), -1, false);
    }

    public void onInitToolbar(Toolbar toolBar, String title) {
        onInitToolbar(toolBar, title, -1, false);
    }

    public void onInitToolbar(Toolbar toolBar, int title) {
        onInitToolbar(toolBar, title, -1, false);
    }

    public void onInitToolbar(Toolbar toolBar, int title, int icon) {
        onInitToolbar(toolBar, getString(title), icon, true);
    }

    public void onInitToolbar(Toolbar toolBar, String title, boolean displayHome) {
        onInitToolbar(toolBar, title, -1, displayHome);
    }

    public void onInitToolbar(Toolbar toolBar, int title, boolean displayHome) {
        onInitToolbar(toolBar, title, -1, displayHome);
    }

    public void onInitToolbar(Toolbar toolBar, int title, int icon, boolean displayHome) {
        onInitToolbar(toolBar, getString(title), icon, displayHome);
    }

    public void onInitToolbar(Toolbar toolBar, String title, int icon, boolean displayHome) {

        if (toolBar != null) {
            setSupportActionBar(toolBar);
            ActionBar actionBar = getSupportActionBar();

            if (actionBar != null) {
                actionBar.setTitle(title);
                actionBar.setDisplayShowHomeEnabled(displayHome);
                actionBar.setDisplayHomeAsUpEnabled(displayHome);
                if (icon != -1 && displayHome) {
                    toolBar.setNavigationIcon(ContextCompat.getDrawable(this, icon));
                }
            }
        }
    }
    //endregion
    protected  class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
        public EMail email;

        private AsyncCallbackOnEventListener<Boolean> mCallBack;
        public Boolean bSuccess = true;
        public Exception mException;
        public SendEmailAsyncTask(AsyncCallbackOnEventListener callback) {
            email= new EMail();
            mCallBack = callback;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                if (email.send()) {
                    //activity.displayMessage(Arrays.toString(email.get_to()) + "로 이메일이 발송되었습니다.");
                    bSuccess = true;
                } else {
                    mException = new Exception("이메일보내기가 실패하였습니다.");
                    return false;
                    // activity.displayMessage(Arrays.toString(email.get_to()) + "로 이메일이 발송이 실패하였습니다.");
                }

                return true;
            } catch (AuthenticationFailedException e) {
                Log.e(SendEmailAsyncTask.class.getName(), "Bad account details");
                e.printStackTrace();

                mException = new Exception("인증실패 하였습니다.");
                return false;
            } catch (MessagingException e) {

                Log.e(SendEmailAsyncTask.class.getName(), "Email failed");
                e.printStackTrace();
                mException = new Exception("이메일보내기가 실패하였습니다.");
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                mException = new Exception("에러가 발생했습니다.");
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if (mCallBack != null) {
                if(true)
                    mCallBack.onSuccess(result);
                else
                    mCallBack.onFailure(mException);
            }
        }
    }
}
