package com.altsoft.loggalapp.Fragement;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.altsoft.Framework.Global;
import com.altsoft.Framework.enResult;
import com.altsoft.Framework.module.BaseFragment;
import com.altsoft.loggalapp.LoginActivity;
import com.altsoft.loggalapp.MainActivity;
import com.altsoft.loggalapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.ss.bottomnavigation.BottomNavigation;

public class TabFragment_Myinfo extends BaseFragment {
    TextView tvUserId;
    TextView tvUserName;
    public View view;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tab_myinfo,container,false);
        tvUserId  = ((TextView) view.findViewById(R.id.tvUserId));;
        tvUserName  = ((TextView) view.findViewById(R.id.tvUserName));;

        final Button btnLogin = view.findViewById(R.id.btnLogin);
        final Button btnLogout = view.findViewById(R.id.btnLogout);

       if(Global.getLoginInfo().isLogin()) {
            btnLogin.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
           tvUserId.setText(Global.getLoginInfo().getData().USER_ID);
           tvUserName.setText(Global.getLoginInfo().getData().USER_NAME);

           ImageView img_profile = view.findViewById(R.id.img_profile);
           if(!Global.getValidityCheck().isEmpty(Global.getLoginInfo().getData().thumnailPath)) {
               Glide.with(Global.getCurrentActivity())
                       .load(Global.getLoginInfo().getData().thumnailPath)
                       .apply(new RequestOptions().override(100, 100))
                       .apply(RequestOptions.circleCropTransform())
                       .into(img_profile)
               ;
           }
        }
        else {
           btnLogin.setVisibility(View.VISIBLE);
           btnLogout.setVisibility(View.GONE);
       }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Global.getCurrentActivity(), LoginActivity.class);
                Global.getCurrentActivity().startActivityForResult(intent, enResult.LoginRequest.getValue());
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Log.d("로그아웃","로그아웃되었음");


                    }
                });
                BottomNavigation bottomNavigation = (BottomNavigation) MainActivity.activity.findViewById(R.id.bottom_navigation);
                tvUserId.setText("");
                tvUserName.setText("");
                bottomNavigation.getTabItems().get(2).setText("내정보");
                ImageView img_profile = view.findViewById(R.id.img_profile);
                Glide.with(Global.getCurrentActivity())
                        .load("")
                        .apply(new RequestOptions().override(100, 100))
                        .into(img_profile)
                ;
                Global.getLoginInfo().setData(null);
                Toast.makeText(
                        Global.getCurrentActivity(),
                        "로그아웃되었습니다.",
                        Toast.LENGTH_LONG).show();
                btnLogin.setVisibility(View.VISIBLE);
                btnLogout.setVisibility(View.GONE);
            }
        });


        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == enResult.LoginRequest.getValue() )
            if (resultCode == -1) {

                return;
            }
    }
}
