package com.altsoft.Interface;

import com.altsoft.dao.AD_SEARCH_COND;
import com.altsoft.dao.DEVICE_LOCATION;
import com.altsoft.dao.DEVICE_LOCATION_COND;
import com.altsoft.dao.T_AD;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MobileService {
    @Headers({
            "Accept: application/vnd.github.v3.full+json",
            "User-Agent: Retrofit-Sample-App"
    })
    @POST("/api/advertising/GetAdList")
    Call<List<T_AD>> GetBannerList (@Body AD_SEARCH_COND Cond);

    @POST("/api/loggalBox/GetDeviceLocation")
    Call<List<DEVICE_LOCATION>> GetDeviceLocation(@Body DEVICE_LOCATION_COND Cond);
}

