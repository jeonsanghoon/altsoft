package com.altsoft.model.UserInfo;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.altsoft.Framework.DataInfo.SaveSharedPreference;
import com.altsoft.Framework.Global;
import com.altsoft.loggalapp.MainActivity;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class LOGIN_DATA implements Serializable {
    public String ERROR_MESSAGE;
    public int    ERROR_TYPE;
    public String USER_ID;
    public String PASSWORD;
    public String USER_NAME;


     public LOGIN_DATA getData()
     {
         LOGIN_DATA rtn = new LOGIN_DATA();
         if(rtn== null) return rtn;
         rtn.USER_ID = Global.getSaveSharedPreference().getData(MainActivity.activity, "USER_ID");
         rtn.PASSWORD = Global.getSaveSharedPreference().getData(MainActivity.activity,"PASSWORD");
         rtn.USER_NAME = Global.getSaveSharedPreference().getData(MainActivity.activity,"USER_NAME");
         return rtn;
     }

    public LOGIN_DATA setData(LOGIN_DATA data)
    {
        if(data == null){Global.getSaveSharedPreference().setData(MainActivity.activity, "USER_ID", null);
            Global.getSaveSharedPreference().setData(MainActivity.activity, "PASSWORD", null);
            Global.getSaveSharedPreference().setData(MainActivity.activity, "USER_NAME", null);}
        else {
            Global.getSaveSharedPreference().setData(MainActivity.activity, "USER_ID", data.USER_ID);
            Global.getSaveSharedPreference().setData(MainActivity.activity, "PASSWORD", data.PASSWORD);
            Global.getSaveSharedPreference().setData(MainActivity.activity, "USER_NAME", data.USER_NAME);
        }
        return data;
    }

    public void LogOut(){
        setData(null);
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Log.d("로그아웃","로그아웃되었음");
                Toast.makeText(
                        Global.getCurrentActivity(),
                        "로그아웃되었습니다.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
