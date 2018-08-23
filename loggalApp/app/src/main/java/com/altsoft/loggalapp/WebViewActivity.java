package com.altsoft.loggalapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.altsoft.Framework.Global;
import com.altsoft.model.MOBILE_AD_DETAIL_COND;
import com.altsoft.model.MOBILE_AD_DETAIL_DATA;
import com.altsoft.model.T_AD;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebViewActivity extends AppCompatActivity {
    private WebView mWebView;

    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        T_AD data = (T_AD) intent.getSerializableExtra("T_AD");
        setTitle(data.TITLE);


        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        //mWebView.loadUrl("http://www.pois.co.kr/mobile/login.do");

        //mWebView.loadUrl("http://www.naver.com"); // 접속 URL
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClientClass());

        MOBILE_AD_DETAIL_COND Cond = new MOBILE_AD_DETAIL_COND();
        Cond.AD_CODE = data.AD_CODE;
        Cond.USER_ID = Global.getLoginInfo().USER_ID;
//        Global.getCommon().ProgressShow(this);

        Call<MOBILE_AD_DETAIL_DATA> call = Global.getAPIService().GetMobileAdDetail(Cond);
        call.enqueue(new Callback<MOBILE_AD_DETAIL_DATA>() {
            @Override
            public void onResponse(Call<MOBILE_AD_DETAIL_DATA> call, Response<MOBILE_AD_DETAIL_DATA> response) {
//                Global.getCommon().ProgressHide(activity);
                mWebView.loadUrl(  response.body().CONTENT_URL);
              //

            }

            @Override
            public void onFailure(Call<MOBILE_AD_DETAIL_DATA> call, Throwable t) {

            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("check URL",url);
            view.loadUrl(url);
            return true;
        }
    }

    //// 뒤로가기 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
