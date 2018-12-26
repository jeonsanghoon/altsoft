package com.altsoft.loggalapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.altsoft.Framework.enResult;
import com.altsoft.Framework.module.BaseActivity;
import com.altsoft.Framework.Global;
import com.altsoft.Framework.map.GpsInfo;
import com.altsoft.Framework.map.MapInfo;
import com.altsoft.loggalapp.Fragement.TabFragment1;
import com.altsoft.loggalapp.Fragement.TabFragment2;
import com.altsoft.loggalapp.Fragement.TabFragment3;
import com.altsoft.loggalapp.databinding.ActivityMainBinding;
import com.altsoft.model.DEVICE_LOCATION;
import com.altsoft.model.T_AD;
import com.altsoft.model.signage.MOBILE_SIGNAGE_LIST;

import java.util.ArrayList;
import java.util.List;

import br.com.liveo.searchliveo.SearchLiveo;
/**
 *
 */
public class MainActivity  extends BaseActivity implements SearchLiveo.OnSearchListener {


    TabFragment1 tab1;
    TabFragment2 tab2;
    TabFragment3 tab3;

    ViewPager viewPager;
    TabLayout tabLayout;

    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.onInitView();
        this.tabInit();
        this.gpsInit();
        this.initViewPager();
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


                switch(tabLayout.getSelectedTabPosition()) {
                    case 1:
                        intent.putExtra("list2", (ArrayList<DEVICE_LOCATION> )TabFragment2.list);
                        intent.putExtra("mapType","localbox");
                        break;
                    case 2:
                        intent.putExtra("list3", (ArrayList<MOBILE_SIGNAGE_LIST> )TabFragment3.list);
                        intent.putExtra("mapType","signage");
                        break;
                    default:
                        intent.putExtra("list1",(ArrayList<T_AD>)TabFragment1.list);
                        intent.putExtra("mapType","banner");
                        break;
                }

                this.startActivityForResult(intent, enResult.Request.getValue());
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            tab1 = null;
            tab1 = new TabFragment1();//.GetBannerList();
            tab2.GetDeviceLocation();
            tab3.GetSignageList();
            return;
        }
    }
    @Override
    public void changedSearch(CharSequence text) {

    }

    private void onInitView() {
        mBinding = (ActivityMainBinding) this.bindView(R.layout.activity_main);
        this.onInitToolbar(mBinding.toolbar, "loggal");

        Global.getCommon().ProgressHide(this);
    }

    private void tabInit() {
        tab1 = new TabFragment1();

        tab2 = new TabFragment2();
        tab3 = new TabFragment3();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, tab1).commit();
        tabLayout= (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("배너정보"));
        tabLayout.addTab(tabLayout.newTab().setText("로컬박스"));
        tabLayout.addTab(tabLayout.newTab().setText("로컬사인"));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Log.d("MainActivity", "선택된 탭 : " + position);

                Fragment selected = null;
                if (position == 0) {
                    selected = tab1;

                } else if (position == 1) {
                    selected = tab2;
                } else if (position == 2) {
                    selected = tab3;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
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
        viewPager = findViewById(R.id.viewPagerMain);

        List<Fragment> listFragments = new ArrayList<>();
        listFragments.add(new TabFragment1());
        listFragments.add(new TabFragment2());
        listFragments.add(new TabFragment3());

        TabPagerAdapter fragmentPagerAdpter = new TabPagerAdapter(getSupportFragmentManager(),listFragments);
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
        });
    }
}