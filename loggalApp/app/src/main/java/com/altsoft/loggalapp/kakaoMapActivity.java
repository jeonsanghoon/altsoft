package com.altsoft.loggalapp;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.altsoft.Adapter.CustomCalloutBalloonAdapter;
import com.altsoft.Framework.Global;
import com.altsoft.Framework.module.BaseActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class kakaoMapActivity extends BaseActivity {
    MapView mapView;
    ViewGroup mapViewContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_map);

        // java code
        mapView = new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);

        Call<JsonObject> call = Global.getKakaoMapAPIService().GetLatiLongiToAddress(Global.getMapInfo().latitude, Global.getMapInfo().longitude);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter("https://altsoft.ze.am/Files/201811/20181121092023.jpg", "테스트"));
                MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Global.getMapInfo().latitude, Global.getMapInfo().longitude);
                mapView.setMapCenterPoint(mapPoint, true);
                //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.
                mapViewContainer.addView(mapView);

                MapPOIItem marker = new MapPOIItem();
                marker.setItemName(Global.getMapInfo().getKakaoAddressName(response)+"|1번");
                marker.setTag(0);
                marker.setMapPoint(mapPoint);
                marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
                marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

                mapView.addPOIItem(marker);

                //CustomCalloutBalloonAdapter obj2 = new CustomCalloutBalloonAdapter("https://altsoft.ze.am/Files/201806/20180612135029.png", "테스트2");

                //mapView.setCalloutBalloonAdapter(obj2);
                mapPoint = MapPoint.mapPointWithGeoCoord(Global.getMapInfo().latitude+ 0.1, Global.getMapInfo().longitude+ 0.1);
                mapView.setMapCenterPoint(mapPoint, true);
                //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.
                //mapViewContainer.addView(mapView);

                marker = new MapPOIItem();
                marker.setItemName("몰라|2번");



                marker.setTag(1);
                marker.setMapPoint(mapPoint);
                marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
                marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

                mapView.addPOIItem(marker);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }
}
