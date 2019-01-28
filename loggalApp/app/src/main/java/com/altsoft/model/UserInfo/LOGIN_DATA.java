package com.altsoft.model.UserInfo;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;
import android.preference.PreferenceManager;

import com.altsoft.Framework.DataInfo.SaveSharedPreference;
import com.altsoft.Framework.Global;

public class LOGIN_DATA implements Serializable {
    public String ERROR_MESSAGE;
    public int    ERROR_TYPE;
    public String USER_ID;
    public String PASSWORD;
    public String USER_NAME;

     public LOGIN_DATA getData()
     {
         LOGIN_DATA rtn = new LOGIN_DATA();
         rtn.USER_ID = Global.getSaveSharedPreference().getData(Global.getCurrentActivity(), "USER_ID");
         rtn.PASSWORD = Global.getSaveSharedPreference().getData(Global.getCurrentActivity(),"PASSWORD");
         rtn.USER_ID = Global.getSaveSharedPreference().getData(Global.getCurrentActivity(),"USER_NAME");
         return rtn;
     }

    public LOGIN_DATA setData(LOGIN_DATA data)
    {
        Global.getSaveSharedPreference().setData(Global.getCurrentActivity(), "USER_ID", data.USER_ID);
        Global.getSaveSharedPreference().setData(Global.getCurrentActivity(),"PASSWORD", data.PASSWORD);
        Global.getSaveSharedPreference().setData(Global.getCurrentActivity(),"USER_NAME", data.USER_NAME);
        return data;
    }
}
