package com.altsoft.Framework.kakao;


import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.altsoft.Framework.Global;
import com.altsoft.loggalapp.MemberJoinActivity;
import com.altsoft.model.UserInfo.LOGIN_COND;
import com.altsoft.model.UserInfo.LOGIN_DATA;
import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionCallback implements ISessionCallback {
    // 로그인에 성공한 상태

    @Override
    public void onSessionOpened() {

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
                Cond.bSnsLogin = true;
                this.LoginExec(Cond);
              //  Global.getLoginInfo().
            }

            private void LoginExec(final LOGIN_COND Cond) {

                Call<LOGIN_DATA> call = Global.getAPIService().GetMemberLogin(Cond);
                call.enqueue(new Callback<LOGIN_DATA>() {
                    @Override
                    public void onResponse(Call<LOGIN_DATA> call, Response<LOGIN_DATA> response) {
                        LOGIN_DATA data = response.body();

                        if(data.ERROR_MESSAGE=="") {
                            /*아이디로 */
                            Toast.makeText(
                                    Global.getCurrentActivity(),
                                    data.USER_NAME + "님이 로그인되었습니다.",
                                    Toast.LENGTH_LONG).show();
                        }
                        else {
                            if(Cond.bSnsLogin) {
                                Intent intent = new Intent(Global.getCurrentActivity(), MemberJoinActivity.class);
                                intent.putExtra("KAKAO_ID", Cond.KAKAO_ID.toString());
                                Global.getCurrentActivity().startActivity(intent);
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

