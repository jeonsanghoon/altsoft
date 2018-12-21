package com.altsoft.loggalapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;


import com.altsoft.Adapter.CustomCalloutBalloonAdapter;
import com.altsoft.Adapter.KakayAddressAdapter;
import com.altsoft.Adapter.SearchAdapter;
import com.altsoft.Framework.Global;
import com.altsoft.Framework.control.altAutoCmpleateTextView;
import com.altsoft.Framework.enResult;
import com.altsoft.Framework.map.MapInfo;
import com.altsoft.Framework.module.BaseActivity;
import com.altsoft.model.daummap.DAUM_ADDRESS;
import com.altsoft.model.keyword.CODE_DATA;
import com.altsoft.model.keyword.KEYWORD_COND;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class kakaoMapActivity extends BaseActivity implements MapView.MapViewEventListener {
    MapView mapView;
    ViewGroup mapViewContainer;
    MapPoint mapPoint;
    MapPOIItem marker;
    Boolean bFirst = true;
    private static final String LOG_TAG = "kakaoMapActivity";
    SearchAutoCompleate searchAutoCompleate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_map);

        // java code
        mapView = new MapView(this);
        mapView.setMapViewEventListener(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        //mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter("https://altsoft.ze.am/Files/201811/20181121092023.jpg", "테스트"));
        mapPoint = MapPoint.mapPointWithGeoCoord(Global.getMapInfo().latitude, Global.getMapInfo().longitude);
        mapView.setMapCenterPoint(mapPoint, true);
        mapViewContainer.addView(mapView);

        this.SetMarkerAddress(Global.getMapInfo().latitude, Global.getMapInfo().longitude);


        /*
        Call<JsonObject> call = Global.getKakaoMapAPIService().GetLatiLongiToAddress(Global.getMapInfo().latitude, Global.getMapInfo().longitude);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.
                MapPOIItem marker = new MapPOIItem();
                marker.setItemName("기준위치"); //Global.getMapInfo().getKakaoAddressName(response)
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
                marker.setItemName("몰라|2번|https://altsoft.ze.am/Files/201803/20180322091519.jpg");

                marker.setTag(1);
                marker.setMapPoint(mapPoint);
                marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
                marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

                mapView.addPOIItem(marker);


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });*/
        searchAutoCompleate = new SearchAutoCompleate();
        this.setUpViews();
        android.support.v7.widget.Toolbar mToolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.tb_toolbarsearch);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    protected void setUpViews() {
        searchAutoCompleate.setUpViews();
        Button search = (Button)findViewById(R.id.btnSearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetMarker();
            }
        });
        Global.getCommon().ProgressHide(this);
        ImageButton mylocationsearch = (ImageButton)findViewById(R.id.btnMylocation);
        mylocationsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetMyLocation();
            }
        });

    }

    private void SetMyLocation() {

        //Global.setMapInfo(new MapInfo( Global.getGpsInfo().getLatitude(), Global.getGpsInfo().getLongitude()));
        searchAutoCompleate.moveMarker(Global.getGpsInfo().getLatitude(), Global.getGpsInfo().getLongitude());
    }

    private void SetMarker() {
        Global.setMapInfo(new MapInfo(mapPoint.getMapPointGeoCoord().latitude,mapPoint.getMapPointGeoCoord().longitude));
        setResult(RESULT_OK);
        finish();
    }

    private void SetMarkerAddress(double latitude, double longitude) {
        Call<JsonObject> call = Global.getKakaoMapAPIService().GetLatiLongiToAddress(latitude, longitude);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(bFirst) {
                    marker = new MapPOIItem();
                    marker.setItemName("기준위치 > " + Global.getMapInfo().getKakaoAddressName(response)); /* */
                    marker.setTag(0);
                    marker.setMapPoint(mapPoint);
                    marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
                    marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

                    mapView.addPOIItem(marker);
                    bFirst = false;;
                }else {
                    MapPOIItem[] poliItems = mapView.getPOIItems();
                    if (poliItems.length > 0) {
                        poliItems[0].setItemName("기준위치 > " + Global.getMapInfo().getKakaoAddressName(response));

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
        try {
            MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
            Log.i(LOG_TAG, String.format("MapView onMapViewCenterPointMoved (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
        } catch (Exception ex) {
        }
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        searchAutoCompleate.moveMarker(mapPoint.getMapPointGeoCoord().latitude, mapPoint.getMapPointGeoCoord().longitude);

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        searchAutoCompleate.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();;
        searchAutoCompleate = null;
    }



    /// 자동완성
    private  class SearchAutoCompleate{
        KakayAddressAdapter adapter;
        List<DAUM_ADDRESS> list;          // 데이터를 넣은 리스트변수
        altAutoCmpleateTextView autoCompleteTextView ;
        Boolean bAutoDrop = false;
        String beforeData = "";


        private SearchAutoCompleate(){}

        private void onResume() {
            Global.getCommon().hideSoftInputWindow( Global.getCurrentActivity(), autoCompleteTextView, true);
        }


        private void setUpViews() {

            list = new ArrayList<DAUM_ADDRESS>();
            autoCompleteTextView = (altAutoCmpleateTextView) Global.getCurrentActivity().findViewById(R.id.autoCompleteTextView);
            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    autoCompleteTextView.dismissDropDown();

                    adapter.setSelectedItem(adapter.getObject(position));

                    moveMarker(adapter.getObject(position).latitude,adapter.getObject(position).longitude);


                }
            });
            autoCompleteTextView.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // SearchAutoCompleate.beforeData = s.toString();
                    bAutoDrop = true;
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String data = s.toString();
                     if(!beforeData.equals(data)) {
                        if (data.length() > 0) {
                            settingList(data);
                        }
                        if(adapter != null) {
                            if(adapter.getSelectedItem().address_name !=null && !(adapter.getSelectedItem().address_name.equals(s.toString())))
                            {
                                adapter.setSelectedItem(new DAUM_ADDRESS());
                            }
                        }
                    }
                    beforeData = data;
                }
            });
        }

        private void moveMarker(Double latitude, Double longitude)
        {
            mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);
            mapView.moveCamera(CameraUpdateFactory.newMapPoint(mapPoint));
            MapPOIItem[] poliItems = mapView.getPOIItems();
            if (poliItems.length > 0) {
                poliItems[0].setMapPoint(mapPoint);
            }
            MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
            SetMarkerAddress( latitude, longitude);
            bAutoDrop = false;
        }

        /// 자동완성 값 셋팅
        private void settingList(String query){
            list = new ArrayList<DAUM_ADDRESS>();


            try {
                Call<JsonObject> call = Global.getKakaoMapAPIService().GetAddressSearch(query);

                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        list = new ArrayList<DAUM_ADDRESS>();
                        if (response.body() != null) {
                            JsonObject rtn = response.body();
                            for (JsonElement data : rtn.getAsJsonArray("documents")) {
                                DAUM_ADDRESS obj = new DAUM_ADDRESS();

                                obj.address_name = data.getAsJsonObject().get("address_name").getAsString();

                                if (data.getAsJsonObject().get("road_address").isJsonNull()) {
                                    obj.road_address_name = obj.address_name;
                                } else
                                    obj.road_address_name = data.getAsJsonObject().get("road_address").getAsJsonObject().get("address_name").getAsString();

                                obj.latitude = data.getAsJsonObject().get("y").getAsDouble();
                                obj.longitude = data.getAsJsonObject().get("x").getAsDouble();
                                obj.zip_code = data.getAsJsonObject().get("address").getAsJsonObject().get("zip_code").getAsString();
                                list.add(obj);
                                //list.add(data.NAME);
                            }
                        }
                        adapter = new KakayAddressAdapter(Global.getCurrentActivity(), R.layout.autocomplate_list_item, list);
                        autoCompleteTextView.setAdapter(adapter);
                        if (bAutoDrop && list.size() >0) {
                            autoCompleteTextView.showDropDown();
                        }

                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });

            }catch(Exception ex) {
                Log.d("로그", ex.getMessage());
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
