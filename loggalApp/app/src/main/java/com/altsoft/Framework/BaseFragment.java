package com.altsoft.Framework;

import android.support.v4.app.Fragment;

import com.altsoft.Interface.MobileService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseFragment  extends Fragment {
    protected MobileService service ;
    public BaseFragment(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.altsoft.ze.am")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(MobileService.class);
    }
}
