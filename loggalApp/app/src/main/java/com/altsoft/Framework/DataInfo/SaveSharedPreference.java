package com.altsoft.Framework.DataInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {


     private SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // 계정 정보 저장
    public  void setData(Context ctx, String Key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(Key, value);
        editor.commit();
    }

    // 저장된 정보 가져오기
    public  String getData(Context ctx, String Key) {
        return getSharedPreferences(ctx).getString(Key, "");
    }

    // 로그아웃
    public  void clearData(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}
