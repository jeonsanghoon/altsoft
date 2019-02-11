package com.altsoft.loggalapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.altsoft.Framework.enResult;
import com.altsoft.Framework.module.BaseActivity;
import com.altsoft.model.RTN_SAVE_DATA;
import com.altsoft.model.UserInfo.LOGIN_COND;
import com.altsoft.model.UserInfo.LOGIN_DATA;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
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
                Global.getCurrentActivity().startActivityForResult(intent, enResult.MemberJoin.getValue());
            }
        });

        btn_kakao_login = (LoginButton) findViewById(R.id.btn_kakao_login);
        btn_kakao_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Session session = Session.getCurrentSession();
                session.addCallback(new SessionCallback());
                session.open(AuthType.KAKAO_TALK_ONLY, LoginActivity.this);


            }
        });
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.getCommon().ProgressShow();
                LOGIN_COND Cond = new LOGIN_COND();
                Cond.USER_ID = ((EditText)findViewById(R.id.lEditEmail)).getText().toString();
                Cond.PASSWORD = ((EditText)findViewById(R.id.lEditPassword)).getText().toString();
                Cond.PASSWORD = Global.getSecurityInfo().ConvertSha( Cond.PASSWORD,SecurityInfo.enSecType.SHA1 );
                Call<LOGIN_DATA> call =  Global.getAPIService().GetMobileLogin(Cond);
                call.enqueue(new Callback<LOGIN_DATA>() {
                    @Override
                    public void onResponse(Call<LOGIN_DATA> call, Response<LOGIN_DATA> response) {
                        LOGIN_DATA rtn = response.body();

                        if(!rtn.ERROR_MESSAGE.equals("") ){
                            Toast.makeText(
                                    getApplicationContext(),
                                    rtn.ERROR_MESSAGE,
                                    Toast.LENGTH_LONG).show();
                        }
                        else {
                            Global.getLoginInfo().setData(response.body());

                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("result",rtn);
                            setResult(RESULT_OK,resultIntent);
                            finish();
                        }
                        Global.getCommon().ProgressHide();
                    }
                    @Override
                    public void onFailure(Call<LOGIN_DATA> call, Throwable t) {
                        Global.getCommon().ProgressHide();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == enResult.MemberJoin.getValue()){
            if (resultCode == RESULT_OK) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", (LOGIN_DATA)data.getSerializableExtra("result"));
                setResult(RESULT_OK,resultIntent);
                finish();
            }
        }
        //간편로그인시 호출 ,없으면 간편로그인시 로그인 성공화면으로 넘어가지 않음
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            //이부분의 사용용도가 궁금합니다.
            return;
        }
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }
    public class SessionCallback implements ISessionCallback {
        // 로그인에 성공한 상태

        @Override
        public void onSessionOpened() {
            requestMe();
        }

        // 로그인에 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
        }


        // 사용자 정보 요청

        public void requestMe() {

            // 사용자정보 요청 결과에 대한 Callback

            UserManagement.getInstance().requestMe(new MeResponseCallback() {
                @Override
                public void onSuccess(UserProfile userProfile) {
                    Log.e("SessionCallback :: ", "onSuccess");
                    String nickname = userProfile.getNickname();
                    String email = userProfile.getEmail();
                    String profileImagePath = userProfile.getProfileImagePath();
                    String thumnailPath = userProfile.getThumbnailImagePath();
                    String UUID = userProfile.getUUID();
                    final long id = userProfile.getId();
                    Log.e("Profile : ", nickname + "");
                    Log.e("Profile : ", email + "");
                    Log.e("Profile : ", profileImagePath + "");
                    Log.e("Profile : ", thumnailPath + "");
                    Log.e("Profile : ", UUID + "");
                    Log.e("Profile : ", id + "");

                    LOGIN_COND Cond = new LOGIN_COND();
                    Cond.KAKAO_ID = Long.toString(id);
                    Cond.profileImagePath = profileImagePath;
                    Cond.thumnailPath = thumnailPath;
                    Cond.bSnsLogin = true;
                    this.LoginExec(Cond);
                    //  Global.getLoginInfo().
                }

                private void LoginExec(final LOGIN_COND Cond) {

                    Call<LOGIN_DATA> call = Global.getAPIService().GetMobileLogin(Cond);
                    call.enqueue(new Callback<LOGIN_DATA>() {
                        @Override
                        public void onResponse(Call<LOGIN_DATA> call, Response<LOGIN_DATA> response) {
                            LOGIN_DATA data = response.body();

                            if(data.ERROR_MESSAGE.equals("")) {
                                /*아이디로 */
                                Toast.makeText(
                                        Global.getCurrentActivity(),
                                        data.USER_NAME + "님이 로그인되었습니다.",
                                        Toast.LENGTH_LONG).show();
                                Intent resultIntent = new Intent();
                                data.profileImagePath = Cond.profileImagePath;
                                data.thumnailPath = Cond.thumnailPath;
                                Global.getLoginInfo().setData(data);
                                resultIntent.putExtra("result",response.body());
                                Global.getCurrentActivity().setResult(RESULT_OK,resultIntent);
                                Global.getCurrentActivity().finish();
                            }
                            else {
                                if(Cond.bSnsLogin) {
                                    new AlertDialog.Builder(Global.getCurrentActivity())
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setTitle("회원가입")
                                            .setMessage("로그인정보가 없습니다. 회원가입 하시겠습니까?")
                                            .setPositiveButton("예", new DialogInterface.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(Global.getCurrentActivity(), MemberJoinActivity.class);
                                                    intent.putExtra("KAKAO_ID", Cond.KAKAO_ID.toString());
                                                    Global.getCurrentActivity().startActivity(intent);
                                                    dialog.dismiss();
                                                }

                                            })
                                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // 카카오 로그아웃
                                                    UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                                                        @Override
                                                        public void onCompleteLogout() {    }
                                                    });
                                                    // Do nothing
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();



                                }else{
                                    Toast.makeText(
                                            Global.getCurrentActivity(),
                                            data.ERROR_MESSAGE,
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<LOGIN_DATA> call, Throwable t) {

                        }
                    });

                }
                @Override
                public void onSessionClosed(ErrorResult errorResult) {

                    Log.e("SessionCallback :: ", "onSessionClosed : " + errorResult.getErrorMessage());
                }

                @Override
                public void onNotSignedUp() {
                    Log.e("SessionCallback :: ", "onNotSignedUp");

                }

                // 사용자 정보 요청 실패
                @Override
                public void onFailure(ErrorResult errorResult) {
                    Log.e("SessionCallback :: ", "onFailure : " + errorResult.getErrorMessage());
                }
            });
        }
    }

}
