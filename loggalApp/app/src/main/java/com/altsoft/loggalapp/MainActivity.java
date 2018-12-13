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

import com.altsoft.Framework.module.BaseActivity;
import com.altsoft.Framework.Global;
import com.altsoft.Framework.map.GpsInfo;
import com.altsoft.Framework.map.MapInfo;
import com.altsoft.loggalapp.Fragement.TabFragment1;
import com.altsoft.loggalapp.Fragement.TabFragment2;
import com.altsoft.loggalapp.Fragement.TabFragment3;
import com.altsoft.loggalapp.databinding.ActivityMainBinding;

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
    private GpsInfo gps;
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
              //  mBinding.searchLiveo.show();

                Intent intent = new Intent(this, Search2Activity.class);

                this.startActivity(intent);
                return true;
            }
            case R.id.action_map_search: {
                Intent intent = new Intent(this, locationMapActivity.class);

                this.startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void changedSearch(CharSequence text) {

    }

    private void onInitView() {
        mBinding = (ActivityMainBinding) this.bindView(R.layout.activity_main);
        this.onInitToolbar(mBinding.toolbar, "loggal");

/*
        mBinding.searchLiveo.
                with(this).
                removeMinToSearch().
                removeSearchDelay().
                build();
                */
/*
        if (mBinding.includeMain != null) {
            mBinding.includeMain.recyclerView.setHasFixedSize(true);
            mBinding.includeMain.recyclerView.setLayoutManager(new LinearLayoutManager(this));

            mBinding.includeMain.swipeContainer.setEnabled(false);
            mBinding.includeMain.swipeContainer.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent,
                    R.color.colorPrimary, R.color.colorAccent);
        }*/
        Global.getCommon().ProgressHide(this);



       /*ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);*/




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

        gps = new GpsInfo(MainActivity.this);
        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            Global.setMapInfo(new MapInfo(MainActivity.this, latitude, longitude));


            Toast.makeText(
                    getApplicationContext(),
                    "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude,
                    Toast.LENGTH_LONG).show();
        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
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