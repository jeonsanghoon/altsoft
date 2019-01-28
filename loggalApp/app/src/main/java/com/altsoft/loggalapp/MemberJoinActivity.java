package com.altsoft.loggalapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.altsoft.Framework.DataInfo.SecurityInfo;
import com.altsoft.Framework.Global;
import com.altsoft.Framework.module.BaseActivity;
import com.altsoft.model.RTN_SAVE_DATA;
import com.altsoft.model.UserInfo.LOGIN_DATA;
import com.altsoft.model.UserInfo.T_MEMBER;
import com.ss.bottomnavigation.TabItem;

import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberJoinActivity extends BaseActivity {
    /// 로그인 정보
    static class SNS_ID{
        public static String KAKAO_ID = "";
        public static String GOOGLE_ID = "";
        public static String NAVER_ID = "";
        public static String FACEBOOK_ID = "";
    }
    static class InputData {
        public static EditText edEmail;
        public static EditText edPw;
        public static EditText edPw2;
        public static EditText userName;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_join);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        ComponentInit();
    }
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
    private void ComponentInit()
    {
        Intent intent = getIntent();
        SNS_ID.KAKAO_ID = intent.getStringExtra("KAKAO_ID");
        SNS_ID.GOOGLE_ID = intent.getStringExtra("GOOGLE_ID");
        SNS_ID.NAVER_ID = intent.getStringExtra("NAVER_ID");
        SNS_ID.FACEBOOK_ID = intent.getStringExtra("FACEBOOK_ID");
        InputData.edEmail = (EditText) findViewById(R.id.rEditEmail);
        InputData.edPw = (EditText) findViewById(R.id.rEditPassword);
        InputData.edPw2 = (EditText) findViewById(R.id.rEditPassword2);
        InputData.userName = (EditText) findViewById(R.id.rEditUserName);

         findViewById(R.id.btnRegister).setOnClickListener(new Button.OnClickListener() {
             @Override
             public void onClick(View v) {
                 MemberSave();
             }
         });
       /* InputData.edPw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    // 입력이 끝났을 때
                    if (!Global.getValidityCheck().Password(InputData.edPw.getText().toString())) {
                        InputData.edPw.requestFocus();
                    }
                    ;
                }
            }
        });
        InputData.edPw2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if(InputData.edPw.getText().toString().length() > 0)
                    {
                        if(!Global.getValidityCheck().PasswordConfirm(InputData.edPw.getText().toString(), InputData.edPw2.getText().toString()))
                        {
                            InputData.edPw2.requestFocus();
                        }
                    }
                }
            }
        });
        */


    }
    /// 유효성체크
    private T_MEMBER SetMemberSaveParam()
    {

        T_MEMBER param = new T_MEMBER();
        param.USER_ID = InputData.edEmail.getText().toString();
        param.EMAIL = InputData.edEmail.getText().toString();;
        param.KAKAO_ID = SNS_ID.KAKAO_ID;
        param.GOOGLE_ID = SNS_ID.GOOGLE_ID;
        param.NAVER_ID = SNS_ID.NAVER_ID;
        param.FACEBOOK_ID = SNS_ID.FACEBOOK_ID;
        param.PASSWORD =InputData.edPw.getText().toString();
        param.USER_NAME = InputData.userName.getText().toString();
        if(!Global.getValidityCheck().Email(param.USER_ID)) {
           return null;
        }
        if(!Global.getValidityCheck().Password(param.PASSWORD)) {
            return null;
        }

        if(!Global.getValidityCheck().PasswordConfirm(param.PASSWORD,InputData.edPw2.getText().toString())) {
            return null;
        }

        if(param.USER_NAME.length() ==0) {
            Toast.makeText(Global.getCurrentActivity(), "사용자를 입력하세요.", Toast.LENGTH_SHORT).show();
            InputData.userName.requestFocus();
            return null;
        }

        param.PASSWORD = Global.getSecurityInfo().ConvertSha(param.PASSWORD, SecurityInfo.enSecType.SHA1);

        return param;
    }


    private void MemberSave() {
        final T_MEMBER param = SetMemberSaveParam();

        if(param == null) return;
        Call<RTN_SAVE_DATA> call =  Global.getAPIService().SaveMember(param);
        call.enqueue(new Callback<RTN_SAVE_DATA>() {
            @Override
            public void onResponse(Call<RTN_SAVE_DATA> call, Response<RTN_SAVE_DATA> response) {
                RTN_SAVE_DATA rtn = response.body();
                if(rtn.ERROR_MESSAGE != "") {
                    Toast.makeText(Global.getCurrentActivity(),rtn.ERROR_MESSAGE, Toast.LENGTH_LONG).show();
                }
                else {
                    LOGIN_DATA param2 = new LOGIN_DATA();
                    param2.USER_ID = param2.USER_ID;
                    param2.USER_NAME = param2.USER_NAME;
                    Global.getLoginInfo().setData(param2);
                    Activity mActivity = MainActivity.activity;
                    TabItem tbItem = MainActivity.activity.findViewById(R.id.tab_myinfo);
                    tbItem.setText( param2.USER_NAME);
                    Toast.makeText(Global.getCurrentActivity(),"로그인이 되었습니다.", Toast.LENGTH_LONG).show();
                    Global.getCommon().AllActivityClose();
                }
            }
            @Override
            public void onFailure(Call<RTN_SAVE_DATA> call, Throwable t) {

            }
        });
    }
}
