package com.altsoft.loggalapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.altsoft.Framework.DataInfo.SecurityInfo;
import com.altsoft.Framework.enResult;
import com.altsoft.Framework.module.BaseActivity;
import com.altsoft.Framework.Global;
import com.altsoft.Framework.map.MapInfo;
import com.altsoft.loggalapp.Fragement.TabFragment1;
import com.altsoft.loggalapp.Fragement.TabFragment2;
import com.altsoft.loggalapp.Fragement.TabFragment3;

import com.altsoft.loggalapp.Fragement.TabFragment_Myinfo;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.ss.bottomnavigation.BottomNavigation;
import com.ss.bottomnavigation.events.OnSelectedItemChangeListener;

import java.security.NoSuchAlgorithmException;

/**
 *
 */
public class MainActivity  extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {


    TabFragment1 tab1;
    TabFragment2 tab2;
    TabFragment3 tab3;
    private FragmentTransaction transaction;
    ViewPager viewPager;
    BottomNavigation bottomNavigation;
    //TabLayout tabLayout;

    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;

    public  static Activity activity;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        //this.CheckOnline();
        this.onInitView();
        this.tabInit();
        this.gpsInit();
        this.initViewPager();
    }

    private void CheckOnline() {
        if(!Global.getValidityCheck().isOnline())
        {
            Toast.makeText(
                    getApplicationContext(),
                    "인터넷 연결을 확인하시고 다시 실행하세요",
                    Toast.LENGTH_LONG).show();
            // finishAffinity();
            // System.runFinalization()
            // System.exit(0);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.fetchCompanies();
    }

    private void fetchCompanies() {
     /*   mAdapter = new MainAdapter(Company.getCompanies());
        mBinding.includeMain.recycler~iew.setAdapter(mAdapter);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
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
                Intent intent = new Intent(this, Search2Activity.class);
                this.startActivity(intent);
                return true;
            }
            case R.id.action_map_search: {
                //Intent intent = new Intent(this, locationMapActivity.class);
                Intent intent = new Intent(this, kakaoMapActivity.class);

                switch(bottomNavigation.getSelectedItem()) {
                    case 0:
                            intent.putExtra("mapType", "banner");
                            this.startActivityForResult(intent, enResult.Request.getValue());
                        break;
                    case 1:
                        if(!(Global.getData().devicelist == null || Global.getData().devicelist.size() == 0)) {
                            //intent.putExtra("list2", (ArrayList<DEVICE_LOCATION>) Global.getData().devicelist);
                            intent.putExtra("mapType", "localbox");
                            this.startActivityForResult(intent, enResult.Request.getValue());
                        }
                        break;
                 /*  case 2:
                        if(!(Global.getData().signagelist == null || Global.getData().signagelist.size() == 0))
                        {
                            intent.putExtra("list3", (ArrayList<MOBILE_SIGNAGE_LIST>) Global.getData().signagelist);
                            intent.putExtra("mapType", "signage");
                            this.startActivityForResult(intent, enResult.Request.getValue());
                        }
                        break;*/


                }



                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            initViewPager();
           // tab1.GetBannerList();//.GetBannerList();

            //tab2.GetDeviceLocation();
            //tab3.GetSignageList();
            return;
        }
    }


    private void onInitView() {

        setContentView(R.layout.activity_main);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this,android.R.color.white));

        Global.getCommon().ProgressHide(this);
    }

    private void tabInit() {
      /*  tab1 = new TabFragment1();

        tab2 = new TabFragment2();
        tab3 = new TabFragment3();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, tab1).commit();
*/
        bottomNavigation=(BottomNavigation)findViewById(R.id.bottom_navigation);
        bottomNavigation.setDefaultItem(0);
        bottomNavigation.setType(bottomNavigation.TYPE_FIXED);

        bottomNavigation.setOnSelectedItemChangeListener(new OnSelectedItemChangeListener() {
            @Override
            public void onSelectedItemChanged(int itemId) {
                switch (itemId){
                    case R.id.tab_banner:
                        transaction=getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container ,new TabFragment1());
                        break;
                    case R.id.tab_localbox:
                        transaction=getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container,new TabFragment2());
                        break;
                   /* case R.id.tab_signage:
                        transaction=getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container,new TabFragment3());
                        break;*/
                    case R.id.tab_myinfo:
                        if(Global.getLoginInfo().USER_ID == null) {
                            Intent intent = new Intent(Global.getCurrentActivity(), LoginActivity.class);
                            intent.putExtra("mapType", "banner");
                            Global.getCurrentActivity().startActivityForResult(intent, enResult.Request.getValue());
                        }

                        transaction=getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container,new TabFragment_Myinfo());

                        break;
                }
                transaction.commit();
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
       // viewPager = findViewById(R.id.viewPagerMain);

       /* TabPagerAdapter fragmentPagerAdpter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(fragmentPagerAdpter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
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
/*
    public class TabPagerAdapter extends FragmentStatePagerAdapter {

        // Count number of tabs
        private int tabCount;

        public TabPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }


        @Override
        public Fragment getItem(int position) {

            // Returning the current tabs
            switch (position) {
                case 0:
                    TabFragment1 tabFragment1 = new TabFragment1();
                    return tabFragment1;
                case 1:
                    TabFragment2 tabFragment2 = new TabFragment2();
                    return tabFragment2;
                case 2:
                    TabFragment3 tabFragment3 = new TabFragment3();
                    return tabFragment3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }    */
}