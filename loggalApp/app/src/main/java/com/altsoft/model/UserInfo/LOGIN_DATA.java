package com.altsoft.model.UserInfo;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;
import android.preference.PreferenceManager;

public class LOGIN_DATA implements Serializable {
    public String ERROR_MESSAGE;
    public int    ERROR_TYPE;
    public String USER_ID;
    public String USER_NAME;

    public String PREF_USER_ID = "userid";
    public String PREF_USER_NAME = "username";
    private SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // 계정 정보 저장
    public void setUserId(Context ctx, String userId) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_ID, userId);
        USER_ID = userId;
        editor.commit();
    }

    // 저장된 정보 가져오기
    public String getUserId(Context ctx) {
        USER_ID = getSharedPreferences(ctx).getString(PREF_USER_ID, "");
        return USER_ID;
    }

    // 계정 정보 저장
    public void setUserName(Context ctx, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        USER_NAME = userName;
        editor.commit();
    }

    // 저장된 정보 가져오기
    public String getUserName(Context ctx) {
        USER_NAME = getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
        return USER_NAME;
    }
}
