package com.altsoft.Framework;

import android.app.Activity;

import com.altsoft.Framework.map.GpsInfo;
import com.altsoft.Framework.map.MapInfo;
import com.altsoft.Interface.KakaoMapService;
import com.altsoft.Interface.MobileService;
import com.altsoft.model.LOGIN_INFO;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Global {




    static ResourceInfo _resourceInfo;

    public static ResourceInfo getResourceInfo ( ) {
        if (_resourceInfo == null) {
            _resourceInfo = new ResourceInfo();
        }
        return _resourceInfo;
    }

    static Activity _currentactivity;
    public static void setCurrentActivity(Activity activity) {
        _currentactivity = activity;
    }
    public static Activity getCurrentActivity() {
        return _currentactivity;
    }

    static MapInfo _mapInfo;
    public static MapInfo getMapInfo ( ) {
        if (_mapInfo == null) {
            _mapInfo = new MapInfo();
        }
        return _mapInfo;
    }
    public static void setMapInfo(MapInfo val) {
        _mapInfo = val;
    }

    static GpsInfo _gpsInfo;
    public static GpsInfo getGpsInfo ( ) {
        if (_gpsInfo == null) {
            _gpsInfo = new GpsInfo();
        }
        return _gpsInfo;
    }

    static Common _common;
    public static Common getCommon ( ) {
        if (_common == null) {
            _common = new Common();
        }
        return _common;
    }

    static FileInfo _fileInfo;
    public static FileInfo getFileInfo ( ) {
        if (_fileInfo == null) {
            _fileInfo = new FileInfo();
        }
        return _fileInfo;
    }

    static LOGIN_INFO _loginInfo;
    public static LOGIN_INFO getLoginInfo ( ) {
        if (_loginInfo == null) {
            _loginInfo = new LOGIN_INFO();
        }
        return _loginInfo;
    }

    static Data _data;
    public static Data getData ( ) {
        if (_data == null) {
            _data = new Data();
        }
        return _data;
    }

    public static void SetLoginInfo(LOGIN_INFO login) {
        _loginInfo = login;
    }

    static MobileService _apiservice;
    public static MobileService getAPIService()
    {
        if(_apiservice == null)
        {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .addHeader("Accept", "application/vnd.github.v3.full+json")
                            .addHeader("User-Agent", "Retrofit-MobileService");
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
            OkHttpClient client = httpClient.build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.altsoft.ze.am")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            _apiservice = retrofit.create(MobileService.class);
        }
        return _apiservice;
    }

    static KakaoMapService _kakaoService;
    public static KakaoMapService getKakaoMapAPIService()
    {
        if(_kakaoService == null)
        {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .addHeader("Accept", "application/vnd.github.v3.full+json")
                            .addHeader("User-Agent", "Retrofit-DaumMap")
                            .addHeader("Authorization", Global.getResourceInfo().getkakaoRestkey());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });

            OkHttpClient client = httpClient.build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://dapi.kakao.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            _kakaoService = retrofit.create(KakaoMapService.class);
        }
        return _kakaoService;
    }
    static FtpInfo _ftpInfo;
    public static FtpInfo getFtpInfo() throws Exception {
        if(_ftpInfo == null) {
            _ftpInfo = new FtpInfo();
        }
        return _ftpInfo;
    }

    public static FtpInfo setFtpInfo(String host, String username, String pwd, int port) throws Exception {
        _ftpInfo = new FtpInfo(host,username,pwd,port);
        return _ftpInfo;
    }
}
