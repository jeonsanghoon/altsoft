package com.altsoft.loggalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.altsoft.Framework.DataInfo.SecurityInfo;
import com.altsoft.Framework.Global;
import com.altsoft.Framework.module.BaseActivity;
import com.altsoft.model.RTN_SAVE_DATA;
import com.altsoft.model.UserInfo.T_MEMBER;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberJoin2Activity extends BaseActivity {
    /// 로그인 정보
    static class SNS_ID{
        public static String KAKAO_ID = "";
        public static String GOOGLE_ID = "";
        public static String NAVER_ID = "";
        public static String FACEBOOK_ID = "";
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_join2);

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

        findViewById(R.id.btnRegister).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberSave();
            }
        });
    }
    /// 유효성체크
    private Boolean MemberSaveCheck(T_MEMBER param )
    {
        if(!Global.getValidityCheck().Email(param.USER_ID)) {
            return false;
        }
        if(!Global.getValidityCheck().Password(param.PASSWORD)) {
            return false;
        }

        return true;
    }


    private void MemberSave() {
        T_MEMBER param = new T_MEMBER();
        EditText edit = (EditText) findViewById(R.id.rEditEmail);
        param.USER_ID = edit.getText().toString();
        param.EMAIL = edit.getText().toString();
        param.KAKAO_ID = SNS_ID.KAKAO_ID;
        param.GOOGLE_ID = SNS_ID.GOOGLE_ID;
        param.NAVER_ID = SNS_ID.NAVER_ID;
        param.FACEBOOK_ID = SNS_ID.FACEBOOK_ID;
        edit = (EditText) findViewById(R.id.rEditPassword);
        param.PASSWORD = Global.getSecurityInfo().ConvertSha(edit.getText().toString(), SecurityInfo.enSecType.SHA1) ;

        if(!MemberSaveCheck(param))   return;
        Call<RTN_SAVE_DATA> call =  Global.getAPIService().SaveMember(param);
        call.enqueue(new Callback<RTN_SAVE_DATA>() {
            @Override
            public void onResponse(Call<RTN_SAVE_DATA> call, Response<RTN_SAVE_DATA> response) {
                RTN_SAVE_DATA rtn = response.body();
                if(rtn.ERROR_MESSAGE != "") {
                    Toast.makeText(Global.getCurrentActivity(),rtn.ERROR_MESSAGE, Toast.LENGTH_LONG).show();
                }
                else {

                }
            }
            @Override
            public void onFailure(Call<RTN_SAVE_DATA> call, Throwable t) {

            }
        });
    }
}
