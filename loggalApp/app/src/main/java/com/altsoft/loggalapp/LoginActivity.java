package com.altsoft.loggalapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.altsoft.Framework.DataInfo.SecurityInfo;
import com.altsoft.Framework.Global;
import com.altsoft.Framework.kakao.SessionCallback;
import com.altsoft.Framework.module.BaseActivity;
import com.altsoft.model.RTN_SAVE_DATA;
import com.altsoft.model.UserInfo.LOGIN_COND;
import com.altsoft.model.UserInfo.LOGIN_DATA;
import com.kakao.auth.AuthType;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.ss.bottomnavigation.TabItem;

import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class LoginActivity extends BaseActivity {
    private SessionCallback callback;


    private Context mContext;
    private LoginButton btn_kakao_login;
    private Button btnLogin;
    private Button btn_custom_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.tb_toolbarlogin);
        setSupportActionBar(mToolbar);
        mContext = getApplicationContext();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
        kakaoLoginInit();
        String keyHash = Global.getCommon().getKeyHash(this);
    }




    private void kakaoLoginInit()
    {
        findViewById(R.id.btnMemberJoin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Global.getCurrentActivity(), MemberJoinActivity.class);
                Global.getCurrentActivity().startActivity(intent);
            }
        });

        btn_kakao_login = (LoginButton) findViewById(R.id.btn_kakao_login);
        btn_kakao_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Session session = Session.getCurrentSession();
                session.addCallback(new SessionCallback());
                session.open(AuthType.KAKAO_LOGIN_ALL, LoginActivity.this);


            }
        });
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LOGIN_COND Cond = new LOGIN_COND();
                Cond.USER_ID = ((EditText)findViewById(R.id.lEditEmail)).getText().toString();
                Cond.PASSWORD = ((EditText)findViewById(R.id.lEditPassword)).getText().toString();
                Cond.PASSWORD = Global.getSecurityInfo().ConvertSha( Cond.PASSWORD,SecurityInfo.enSecType.SHA1 );
                Call<LOGIN_DATA> call =  Global.getAPIService().GetMemberLogin(Cond);
                call.enqueue(new Callback<LOGIN_DATA>() {
                    @Override
                    public void onResponse(Call<LOGIN_DATA> call, Response<LOGIN_DATA> response) {
                        LOGIN_DATA rtn = response.body();
                        Global.getLoginInfo().setData(response.body());
                        Toast.makeText(
                                getApplicationContext(),
                                rtn.USER_NAME + "님이 로그인하였습니다.",
                                Toast.LENGTH_LONG).show();
                        Global.getCommon().AllActivityClose();
                    }
                    @Override
                    public void onFailure(Call<LOGIN_DATA> call, Throwable t) {

                    }
                });
            }
        });
        btn_custom_logout = (Button) findViewById(R.id.btn_custom_logout);
        btn_custom_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Log.d("로그아웃","로그아웃되었음");
                        Toast.makeText(
                                getApplicationContext(),
                                "로그아웃되었습니다.",
                                Toast.LENGTH_LONG).show();
                    }
                });


            }
        });
        btn_custom_logout.setVisibility(View.GONE);
        btn_kakao_login = (LoginButton) findViewById(R.id.btn_kakao_login);
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

}
