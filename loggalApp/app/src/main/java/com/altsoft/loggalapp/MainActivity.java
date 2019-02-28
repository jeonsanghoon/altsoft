package com.altsoft.loggalapp;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.altsoft.Framework.enResult;
import com.altsoft.Framework.module.BaseActivity;
import com.altsoft.Framework.Global;
import com.altsoft.Framework.map.MapInfo;
import com.altsoft.loggalapp.Fragement.TabFragment_Banner;
import com.altsoft.loggalapp.Fragement.TabFragment_localbox;

import com.altsoft.loggalapp.Fragement.TabFragment_Myinfo;

import com.altsoft.loggalapp.Fragement.TabFragment_localStation;
import com.altsoft.map.kakaoMapActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.ss.bottomnavigation.BottomNavigation;
import com.ss.bottomnavigation.events.OnSelectedItemChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MainActivity  extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    BottomNavigation bottomNavigation;
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isPermission = false;

    public  static Activity activity;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        //this.CheckOnline();
        this.onInitView();
        this.initViewPager();
        this.tabInit();
        this.gpsInit();

        Log.d("hashKey",Global.getCommon().getKeyHash(this));
        getSupportActionBar().setTitle("loggal");
    }
    @Override
    public void onResume() {
        super.onResume();
        this.LoginInfoSet();
    }
    private void CheckOnline() {
        if(!Global.getValidityCheck().isOnline())
        {
            Toast.makeText(
                    getApplicationContext(),
                    "인터넷 연결을 확인하시고 다시 실행하세요",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
        mOptionsMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
            case R.id.action_search: {
                Intent intent = new Intent(this, SearchActivity.class);
                this.startActivity(intent);
                return true;
            }
            case R.id.action_map_search: {
                Intent intent = new Intent(this, kakaoMapActivity.class);

                switch(bottomNavigation.getSelectedItem()) {
                    case 0:
                            intent.putExtra("mapType", "banner");
                            this.startActivityForResult(intent, enResult.BannerRequest.getValue());
                        break;
                    case 1:
                        if(!(Global.getData().devicelist == null || Global.getData().devicelist.size() == 0)) {
                            //intent.putExtra("list2", (ArrayList<DEVICE_LOCATION>) Global.getData().devicelist);
                            intent.putExtra("mapType", "localbox");
                            this.startActivityForResult(intent, enResult.LocalboxRequest.getValue());
                        }
                        break;
                    case 2:
                        if(!(Global.getData().devicelist == null || Global.getData().devicelist.size() == 0)) {
                            //intent.putExtra("list2", (ArrayList<DEVICE_LOCATION>) Global.getData().devicelist);
                            intent.putExtra("mapType", "localstation");
                            this.startActivityForResult(intent, enResult.LoglstationRequest.getValue());
                        }
                        break;

                   /*  case 2:
                        if(!(Global.getData().stationlist == null || Global.getData().stationlist.size() == 0)) {
                            //intent.putExtra("list2", (ArrayList<DEVICE_LOCATION>) Global.getData().devicelist);
                            intent.putExtra("mapType", "locationstation");
                            this.startActivityForResult(intent, enResult.LoglstationRequest.getValue());
                        }
                        break;*/
                   case 3:

                        /*if(!(Global.getData().signagelist == null || Global.getData().signagelist.size() == 0))
                        {
                            intent.putExtra("list3", (ArrayList<MOBILE_SIGNAGE_LIST>) Global.getData().signagelist);
                            intent.putExtra("mapType", "signage");
                            this.startActivityForResult(intent, enResult.Request.getValue());
                        }*/
                        break;
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == enResult.LoginRequest.getValue() )
        if (resultCode == RESULT_OK) {
            //LoginInfoSet();
            return;
        }
    }

    // 로그인정보 셋팅
    private void LoginInfoSet()
    {
        if(Global.getLoginInfo().isLogin()) {
            try {
                bottomNavigation.getTabItems().get(3).setText(Global.getLoginInfo().getData().USER_NAME);
                if(bottomNavigation.getSelectedItem() == 3) {
                    ((TextView) findViewById(R.id.tvUserName)).setText(Global.getLoginInfo().getData().USER_NAME );
                    ((TextView) findViewById(R.id.tvUserId)).setText(Global.getLoginInfo().getData().USER_ID );
                    ImageView img_profile = findViewById(R.id.img_profile);
                    Glide.with(Global.getCurrentActivity())
                            .load(Global.getLoginInfo().getData().thumnailPath)
                            .apply(new RequestOptions().override(100, 100))
                            .apply(RequestOptions.circleCropTransform())
                            .into(img_profile)
                    ;
                    ((Button)findViewById(R.id.btnLogin)).setVisibility(View.GONE);
                    ((Button)findViewById(R.id.btnLogout)).setVisibility(View.VISIBLE);
                }
            }catch(Exception ex){}
        }
    }


    private void onInitView() {

        setContentView(R.layout.activity_main);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this,android.R.color.white));

        Global.getCommon().ProgressHide(this);
    }
    private Menu mOptionsMenu;
    private  void setVisibleMapButton(Boolean isVisible)
    {
        try {
            if(mOptionsMenu != null) {
                MenuItem item = mOptionsMenu.findItem(R.id.action_map_search);
                item.setVisible(isVisible);
            }
        }catch(Exception ex){}
    }


    private void tabInit() {

        bottomNavigation=(BottomNavigation)findViewById(R.id.bottom_navigation);
    //    bottomNavigation.setDefaultItem(0);
        bottomNavigation.setType(bottomNavigation.TYPE_FIXED);

        bottomNavigation.setOnSelectedItemChangeListener(new OnSelectedItemChangeListener() {
            @Override
            public void onSelectedItemChanged(int itemId) {
                setVisibleMapButton(true);

                switch (itemId){
                    case R.id.tab_banner:
                        mViewPager.setCurrentItem(0,false);
                        break;
                    case R.id.tab_localbox:
                        mViewPager.setCurrentItem(1,false);
                        break;
                    case R.id.tab_localstation:
                        mViewPager.setCurrentItem(2,false);
                        break;
                    case R.id.tab_myinfo:
                        mViewPager.setCurrentItem(3,false);
                        if(!Global.getLoginInfo().isLogin()) {
                            Intent intent = new Intent(Global.getCurrentActivity(), LoginActivity.class);
                            Global.getCurrentActivity().startActivityForResult(intent, enResult.LoginRequest.getValue());
                        }
                        setVisibleMapButton(false);
                        if(Global.getLoginInfo().isLogin()) {
                            LoginInfoSet();
                        }
                        break;
                }

            }
        });
     }

    // 위치 권한 요청
    private Boolean callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
        return isPermission;
    }

    private void gpsInit() {
        // 권한 요청을 해야 함
        if(!callPermission()) return;


        // GPS 사용유무 가져오기
        if (Global.getGpsInfo().isGetLocation()) {

            double latitude = Global.getGpsInfo().getLatitude();
            double longitude = Global.getGpsInfo().getLongitude();

            Global.setMapInfo(new MapInfo(MainActivity.this, latitude, longitude));

            Toast.makeText(
                    getApplicationContext(),
                    "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude,
                    Toast.LENGTH_LONG).show();
        } else {
            // GPS 를 사용할수 없으므로
            Global.getGpsInfo().showSettingsAlert();
        }
    }

    private void initViewPager() {
        mViewPager = findViewById(R.id.pager);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());

        mPagerAdapter.addFragment( new TabFragment_Banner());
        mPagerAdapter.addFragment( new TabFragment_localbox());
        mPagerAdapter.addFragment( new TabFragment_localStation());
        mPagerAdapter.addFragment( new TabFragment_Myinfo());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                bottomNavigation.setSelectedItem(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {}
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        ArrayList<android.support.v4.app.Fragment> adapterList = new ArrayList<>();
        public PagerAdapter(FragmentManager fm) {
            super(fm);

        }

        public void addFragment(android.support.v4.app.Fragment frag)
        {
            adapterList.add(frag);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            // Returning the current tabs
            return adapterList.get(position);
        }

        @Override
        public int getCount() {
            return adapterList.size();
        }
    }
}