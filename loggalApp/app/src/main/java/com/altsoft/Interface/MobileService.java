package com.altsoft.Interface;

import com.altsoft.model.AD_SEARCH_COND;
import com.altsoft.model.DEVICE_LOCATION;
import com.altsoft.model.DEVICE_LOCATION_COND;
import com.altsoft.model.MOBILE_AD_DETAIL_COND;
import com.altsoft.model.MOBILE_AD_DETAIL_DATA;
import com.altsoft.model.T_AD;
import com.altsoft.model.device.AD_DEVICE_MOBILE_COND;
import com.altsoft.model.device.AD_DEVICE_MOBILE_M;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
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
    @POST("/api/advertising/GetMobileAdDetail")
    Call<MOBILE_AD_DETAIL_DATA> GetMobileAdDetail(@Body MOBILE_AD_DETAIL_COND Cond);
    @POST("/api/advertising/GetMobileAdDeviceList")
    Call<AD_DEVICE_MOBILE_M> GetMobileAdDeviceList(@Body AD_DEVICE_MOBILE_COND Cond);
}

